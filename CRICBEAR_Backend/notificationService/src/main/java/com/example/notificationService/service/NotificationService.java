package com.example.notificationService.service;

import com.example.notificationService.model.Notification;

import com.example.notificationService.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    public Notification sendNotification(Integer uid, String reminderType, String message) {
        Notification notification = new Notification(uid, reminderType, message, LocalDateTime.now());
        return notificationRepository.save(notification);
    }

    public Notification addNotification(Notification notification) {
        return notificationRepository.save(notification);
    }

    public Notification updateNotification(Integer nid, Notification updatedNotification) {
        Optional<Notification> existingNotificationOpt = notificationRepository.findById(nid);

        if (existingNotificationOpt.isPresent()) {
            Notification existingNotification = existingNotificationOpt.get();
            existingNotification.setReminderType(updatedNotification.getReminderType());
            existingNotification.setMessage(updatedNotification.getMessage());
            existingNotification.setSentAt(updatedNotification.getSentAt());

            return notificationRepository.save(existingNotification);
        } else {
            throw new RuntimeException("Notification not found with ID " + nid);
        }
    }

    public void deleteNotification(Integer nid) {
        notificationRepository.deleteById(nid);
    }


    public List<Notification> getAllNotifications() {
        return notificationRepository.findAll();
    }

    public List<Notification> findWithCurrentDate(LocalDateTime date) {

        List<Notification> filteredNotifications = new ArrayList<>();
//        System.out.println(notifications);
        // Truncate the input date to minutes (ignoring seconds)
        LocalDateTime truncatedInputDate = date.truncatedTo(ChronoUnit.MINUTES);
        List<Notification> notifications = notificationRepository.findWithCurrentDate();
        System.out.println(notifications);
        // Iterate over all notifications and filter by date and time (without seconds)
        for (Notification notification : notifications) {
            LocalDateTime sentAt = notification.getSentAt().truncatedTo(ChronoUnit.MINUTES);

            if (sentAt.equals(truncatedInputDate)) {
                filteredNotifications.add(notification);
            }
        }

        return filteredNotifications;
    }
}