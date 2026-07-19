package com.exe.buddy_english_be.modules.notification.service;

import com.exe.buddy_english_be.modules.notification.dto.NotificationRequest;
import com.exe.buddy_english_be.modules.notification.dto.NotificationResponse;

import java.util.List;

public interface NotificationService {

    // CRUD cơ bản
    List<NotificationResponse> getAllNotifications();

    NotificationResponse getNotificationById(Long id);

    NotificationResponse createNotification(NotificationRequest request);

    NotificationResponse updateNotification(Long id, NotificationRequest request);

    void deleteNotification(Long id);

    // Truy vấn theo child
    List<NotificationResponse> getNotificationsByChildId(Long childId);

    List<NotificationResponse> getUnreadNotificationsByChildId(Long childId);

    long countUnreadByChildId(Long childId);

    // Đánh dấu đã đọc
    NotificationResponse markAsRead(Long id);

    int markAllAsReadByChildId(Long childId);

    // Xóa hàng loạt
    void deleteAllByChildId(Long childId);
}
