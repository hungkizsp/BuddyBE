package com.exe.buddy_english_be.modules.notification.service;

import com.exe.buddy_english_be.modules.notification.dto.NotificationRequest;
import com.exe.buddy_english_be.modules.notification.dto.NotificationResponse;
import com.exe.buddy_english_be.modules.notification.entity.Notification;
import com.exe.buddy_english_be.modules.notification.repository.NotificationRepository;
import com.exe.buddy_english_be.modules.profile.entity.ChildProfile;
import com.exe.buddy_english_be.modules.profile.repository.ChildProfileRepository;
import com.exe.buddy_english_be.shared.exception.BusinessException;
import com.exe.buddy_english_be.shared.exception.ErrorCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final ChildProfileRepository childProfileRepository;

    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            ChildProfileRepository childProfileRepository) {
        this.notificationRepository = notificationRepository;
        this.childProfileRepository = childProfileRepository;
    }

    // ─── CRUD cơ bản ──────────────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getAllNotifications() {
        return notificationRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse getNotificationById(Long id) {
        return toResponse(findNotification(id));
    }

    @Override
    @Transactional
    public NotificationResponse createNotification(NotificationRequest request) {
        ChildProfile child = findChild(request.childId());

        Notification notification = Notification.builder()
                .child(child)
                .title(request.title().trim())
                .message(request.message().trim())
                .type(normalize(request.type()))
                .isRead(request.isRead() != null ? request.isRead() : false)
                .build();

        return toResponse(notificationRepository.save(notification));
    }

    @Override
    @Transactional
    public NotificationResponse updateNotification(Long id, NotificationRequest request) {
        Notification notification = findNotification(id);
        ChildProfile child = findChild(request.childId());

        notification.setChild(child);
        notification.setTitle(request.title().trim());
        notification.setMessage(request.message().trim());
        notification.setType(normalize(request.type()));
        if (request.isRead() != null) {
            notification.setIsRead(request.isRead());
        }

        return toResponse(notificationRepository.save(notification));
    }

    @Override
    @Transactional
    public void deleteNotification(Long id) {
        notificationRepository.delete(findNotification(id));
    }

    // ─── Truy vấn theo child ──────────────────────────────────────────────────

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotificationsByChildId(Long childId) {
        validateChildExists(childId);
        return notificationRepository.findByChildIdOrderByCreatedAtDesc(childId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadNotificationsByChildId(Long childId) {
        validateChildExists(childId);
        return notificationRepository.findByChildIdAndIsReadFalseOrderByCreatedAtDesc(childId).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public long countUnreadByChildId(Long childId) {
        validateChildExists(childId);
        return notificationRepository.countByChildIdAndIsReadFalse(childId);
    }

    // ─── Đánh dấu đã đọc ─────────────────────────────────────────────────────

    @Override
    @Transactional
    public NotificationResponse markAsRead(Long id) {
        Notification notification = findNotification(id);
        notification.setIsRead(true);
        return toResponse(notificationRepository.save(notification));
    }

    @Override
    @Transactional
    public int markAllAsReadByChildId(Long childId) {
        validateChildExists(childId);
        return notificationRepository.markAllAsReadByChildId(childId);
    }

    // ─── Xóa hàng loạt ───────────────────────────────────────────────────────

    @Override
    @Transactional
    public void deleteAllByChildId(Long childId) {
        validateChildExists(childId);
        notificationRepository.deleteAllByChildId(childId);
    }

    // ─── Helper methods ───────────────────────────────────────────────────────

    private Notification findNotification(Long id) {
        return notificationRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOTIFICATION_NOT_FOUND));
    }

    private ChildProfile findChild(Long id) {
        return childProfileRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.CHILD_PROFILE_NOT_FOUND));
    }

    private void validateChildExists(Long childId) {
        if (!childProfileRepository.existsById(childId)) {
            throw new BusinessException(ErrorCode.CHILD_PROFILE_NOT_FOUND);
        }
    }

    private NotificationResponse toResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getChild() != null ? notification.getChild().getId() : null,
                notification.getTitle(),
                notification.getMessage(),
                notification.getType(),
                notification.getIsRead(),
                notification.getCreatedAt(),
                notification.getUpdatedAt()
        );
    }

    private String normalize(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }
}
