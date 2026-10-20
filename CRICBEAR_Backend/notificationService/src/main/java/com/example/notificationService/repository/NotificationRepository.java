package com.example.notificationService.repository;



import com.example.notificationService.model.Notification;
import com.example.notificationService.repository.NotificationRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
    //Select a date with current time.
    @Query("SELECT n FROM Notification n WHERE FUNCTION('DATE', n.sentAt) = CURRENT_DATE AND FUNCTION('to_char', n.sentAt, 'HH24:MI') = FUNCTION('to_char', CURRENT_TIMESTAMP, 'HH24:MI')")
    List<Notification> findWithCurrentDate();
}