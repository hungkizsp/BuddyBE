package com.exe.buddy_english_be.modules.notification.repository;

import com.exe.buddy_english_be.modules.notification.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByChildIdOrderByCreatedAtDesc(Long childId);

    List<Notification> findByChildIdAndIsReadFalseOrderByCreatedAtDesc(Long childId);

    long countByChildIdAndIsReadFalse(Long childId);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.child.id = :childId AND n.isRead = false")
    int markAllAsReadByChildId(@Param("childId") Long childId);

    @Modifying
    @Query("DELETE FROM Notification n WHERE n.child.id = :childId")
    void deleteAllByChildId(@Param("childId") Long childId);
}
