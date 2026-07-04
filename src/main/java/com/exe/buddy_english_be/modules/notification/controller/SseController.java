package com.exe.buddy_english_be.modules.notification.controller;

import com.exe.buddy_english_be.modules.notification.sse.NotificationSseEmitter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/sse/notifications")
public class SseController {

    private final NotificationSseEmitter notificationSseEmitter;

    public SseController(NotificationSseEmitter notificationSseEmitter) {
        this.notificationSseEmitter = notificationSseEmitter;
    }

    @GetMapping(value = "/{childId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribe(@PathVariable Long childId) {
        // Set timeout to 5 minutes
        SseEmitter emitter = new SseEmitter(TimeUnit.MINUTES.toMillis(5));

        notificationSseEmitter.addEmitter(childId, emitter);

        try {
            // Send connection established event
            emitter.send(SseEmitter.event()
                    .name("CONNECTED")
                    .data("SSE connection established for child " + childId));
        } catch (IOException e) {
            emitter.completeWithError(e);
        }

        return emitter;
    }
}
