package com.exe.buddy_english_be.modules.notification.sse;

import com.exe.buddy_english_be.modules.notification.dto.NotificationResponse;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class NotificationSseEmitter {

    // Map of childId -> list of SseEmitters
    private final Map<Long, List<SseEmitter>> emitters = new ConcurrentHashMap<>();

    public void addEmitter(Long childId, SseEmitter emitter) {
        emitters.computeIfAbsent(childId, k -> new CopyOnWriteArrayList<>()).add(emitter);

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
                } catch (IOException e) {
                    emitter.complete();
                    removeEmitter(childId, emitter);
                }
            }
        }
    }

    // Keep connections alive
    @Scheduled(fixedRate = 25000)
    public void sendHeartbeat() {
        emitters.forEach((childId, childEmitters) -> {
            for (SseEmitter emitter : childEmitters) {
                try {
                    // Sending a comment just to keep the connection alive
                    emitter.send(SseEmitter.event().comment("heartbeat"));
                } catch (IOException e) {
                    emitter.complete();
                    removeEmitter(childId, emitter);
                }
            }
        });
    }
}
