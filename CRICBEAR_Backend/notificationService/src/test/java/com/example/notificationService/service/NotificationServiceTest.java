package com.example.notificationService.service;

import com.example.notificationService.model.Notification;
import com.example.notificationService.repository.NotificationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private NotificationService notificationService;

    @Test
    void testSendNotification() {
        Notification notification = new Notification(1, "EMAIL", "Test message", LocalDateTime.now());

        when(notificationRepository.save(notification)).thenReturn(notification);

        Notification result = notificationService.sendNotification(1, "EMAIL", "Test message");

        assertEquals(notification.getUid(), result.getUid());
        assertEquals(notification.getReminderType(), result.getReminderType());
        assertEquals(notification.getMessage(), result.getMessage());
    }

    @Test
    void testAddNotification() {
        Notification notification = new Notification(1, "sms", "Another test message", LocalDateTime.now());
        when(notificationRepository.save(any(Notification.class))).thenReturn(notification);

        Notification result = notificationService.addNotification(notification);

        assertNotNull(result);
        assertEquals("sms", result.getReminderType());
        assertEquals("Another test message", result.getMessage());
        verify(notificationRepository, times(1)).save(notification);
    }

    @Test
    void testUpdateNotification() {
        Notification existingNotification = new Notification(1, "email", "Old message", LocalDateTime.now());
        Notification updatedNotification = new Notification(1, "sms", "Updated message", LocalDateTime.now());
        when(notificationRepository.findById(1)).thenReturn(Optional.of(existingNotification));
        when(notificationRepository.save(any(Notification.class))).thenReturn(updatedNotification);

        Notification result = notificationService.updateNotification(1, updatedNotification);

        assertNotNull(result);
        assertEquals("sms", result.getReminderType());
        assertEquals("Updated message", result.getMessage());
        verify(notificationRepository, times(1)).findById(1);
        verify(notificationRepository, times(1)).save(existingNotification);
    }

    @Test
    void testUpdateNotification_NotFound() {
        Notification updatedNotification = new Notification(1, "sms", "Updated message", LocalDateTime.now());
        when(notificationRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            notificationService.updateNotification(1, updatedNotification);
        });

        assertEquals("Notification not found with ID 1", exception.getMessage());
        verify(notificationRepository, times(1)).findById(1);
        verify(notificationRepository, times(0)).save(any(Notification.class));
    }

    @Test
    void testDeleteNotification() {
        doNothing().when(notificationRepository).deleteById(1);

        notificationService.deleteNotification(1);

        verify(notificationRepository, times(1)).deleteById(1);
    }

    @Test
    void testGetAllNotifications() {
        Notification notification1 = new Notification(1, "EMAIL", "Test message 1", LocalDateTime.now());
        Notification notification2 = new Notification(2, "SMS", "Test message 2", LocalDateTime.now());
        List<Notification> notifications = Arrays.asList(notification1, notification2);

        when(notificationRepository.findAll()).thenReturn(notifications);

        List<Notification> result = notificationService.getAllNotifications();

        assertEquals(2, result.size());
        assertEquals("EMAIL", result.get(0).getReminderType());
        assertEquals("SMS", result.get(1).getReminderType());
    }
}

