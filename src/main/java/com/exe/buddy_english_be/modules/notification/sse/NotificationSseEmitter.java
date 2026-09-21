package com.exe.buddy_english_be.modules.notification.sse;

import com.exe.buddy_english_be.modules.notification.dto.NotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class NotificationSseEmitter {

    private static final Logger log = LoggerFactory.getLogger(NotificationSseEmitter.class);

    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public void addEmitter(Long childId, SseEmitter emitter) {
        emitters
                .computeIfAbsent(childId, k -> new CopyOnWriteArrayList<>())
                .add(emitter);

        emitter.onCompletion(() -> removeEmitter(childId, emitter));
        emitter.onTimeout(() -> removeEmitter(childId, emitter));
        emitter.onError(e -> removeEmitter(childId, emitter));
    }

    private void removeEmitter(Long childId, SseEmitter emitter) {
        List<SseEmitter> childEmitters = emitters.get(childId);

        if (childEmitters != null) {
            childEmitters.remove(emitter);

            if (childEmitters.isEmpty()) {
                emitters.remove(childId);
            }
        }
    }

    public void send(Long childId, NotificationResponse notification) {
        List<SseEmitter> childEmitters = emitters.get(childId);

        if (childEmitters != null) {
            for (SseEmitter emitter : childEmitters) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("NEW_NOTIFICATION")
                            .data(notification));
                } catch (Exception e) {
                    log.debug("Removing dead SSE emitter for child {}: {}", childId, e.getMessage());
                    removeEmitter(childId, emitter);
                }
            }
        }
    }

    @Scheduled(fixedRate = 25000)
    public void sendHeartbeat() {
        emitters.forEach((childId, childEmitters) -> {
            for (SseEmitter emitter : childEmitters) {
                try {
                    emitter.send(
                            SseEmitter.event()
                                    .comment("heartbeat"));
                } catch (Exception e) {
                    log.debug("Removing dead SSE emitter during heartbeat for child {}: {}", childId, e.getMessage());
                    removeEmitter(childId, emitter);
                }
            }
        });
    }
}
