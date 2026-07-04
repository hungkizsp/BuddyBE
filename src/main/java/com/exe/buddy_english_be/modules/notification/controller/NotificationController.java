package com.exe.buddy_english_be.modules.notification.controller;

import com.exe.buddy_english_be.modules.notification.dto.NotificationRequest;
import com.exe.buddy_english_be.modules.notification.dto.NotificationResponse;
import com.exe.buddy_english_be.modules.notification.service.NotificationService;
import com.exe.buddy_english_be.shared.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController("modulesNotificationController")
@RequestMapping("/api/notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    // ─── CRUD cơ bản ──────────────────────────────────────────────────────────

    @GetMapping
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getAllNotifications() {
        List<NotificationResponse> response = notificationService.getAllNotifications();
        return ResponseEntity.ok(ApiResponse.success("Notifications fetched successfully", response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationResponse>> getNotificationById(@PathVariable Long id) {
        NotificationResponse response = notificationService.getNotificationById(id);
        return ResponseEntity.ok(ApiResponse.success("Notification fetched successfully", response));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<NotificationResponse>> createNotification(
            @Valid @RequestBody NotificationRequest request) {
        NotificationResponse response = notificationService.createNotification(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("Notification created successfully", response));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<NotificationResponse>> updateNotification(
            @PathVariable Long id,
            @Valid @RequestBody NotificationRequest request) {
        NotificationResponse response = notificationService.updateNotification(id, request);
        return ResponseEntity.ok(ApiResponse.success("Notification updated successfully", response));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteNotification(@PathVariable Long id) {
        notificationService.deleteNotification(id);
        return ResponseEntity.ok(ApiResponse.success("Notification deleted successfully", null));
    }

    // ─── Truy vấn theo child ──────────────────────────────────────────────────

    @GetMapping("/child/{childId}")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getNotificationsByChildId(
            @PathVariable Long childId) {
        List<NotificationResponse> response = notificationService.getNotificationsByChildId(childId);
        return ResponseEntity.ok(ApiResponse.success("Notifications fetched successfully", response));
    }

    @GetMapping("/child/{childId}/unread")
    public ResponseEntity<ApiResponse<List<NotificationResponse>>> getUnreadNotificationsByChildId(
            @PathVariable Long childId) {
        List<NotificationResponse> response = notificationService.getUnreadNotificationsByChildId(childId);
        return ResponseEntity.ok(ApiResponse.success("Unread notifications fetched successfully", response));
    }

    @GetMapping("/child/{childId}/unread-count")
    public ResponseEntity<ApiResponse<Map<String, Long>>> countUnreadByChildId(
            @PathVariable Long childId) {
        long count = notificationService.countUnreadByChildId(childId);
        return ResponseEntity.ok(ApiResponse.success("Unread count fetched successfully", Map.of("unreadCount", count)));
    }

    // ─── Đánh dấu đã đọc ─────────────────────────────────────────────────────

    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse<NotificationResponse>> markAsRead(@PathVariable Long id) {
        NotificationResponse response = notificationService.markAsRead(id);
        return ResponseEntity.ok(ApiResponse.success("Notification marked as read", response));
    }

    @PatchMapping("/child/{childId}/read-all")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> markAllAsReadByChildId(
            @PathVariable Long childId) {
        int updated = notificationService.markAllAsReadByChildId(childId);
        return ResponseEntity.ok(ApiResponse.success("All notifications marked as read",
                Map.of("updatedCount", updated)));
    }

    // ─── Xóa hàng loạt ───────────────────────────────────────────────────────

    @DeleteMapping("/child/{childId}")
    public ResponseEntity<ApiResponse<Void>> deleteAllByChildId(@PathVariable Long childId) {
        notificationService.deleteAllByChildId(childId);
        return ResponseEntity.ok(ApiResponse.success("All notifications for child deleted successfully", null));
    }
}
