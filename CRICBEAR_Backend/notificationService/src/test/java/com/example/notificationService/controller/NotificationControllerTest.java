package com.example.notificationService.controller;

import com.example.notificationService.model.Notification;
import com.example.notificationService.service.NotificationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static net.bytebuddy.matcher.ElementMatchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(NotificationController.class)
class NotificationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private NotificationService notificationService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void testSendNotification() throws Exception {
        // Create a sample notification object
        Notification notification = new Notification(1, "EMAIL", "Test message", LocalDateTime.now());

        // Mock the service method to return a list containing the sample notification
        when(notificationService.findWithCurrentDate(any(LocalDateTime.class)))
                .thenReturn(Collections.singletonList(notification));

        // Perform the POST request
        mockMvc.perform(post("/notifications/send"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uid").value(1))
                .andExpect(jsonPath("$[0].reminderType").value("EMAIL"))
                .andExpect(jsonPath("$[0].message").value("Test message"));
    }

    @Test
    void testAddNotification() throws Exception {
        Notification notification = new Notification(1, "sms", "Test message", LocalDateTime.now());
        when(notificationService.addNotification(any(Notification.class))).thenReturn(notification);

        mockMvc.perform(post("/notifications/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"uid\":1,\"reminderType\":\"sms\",\"message\":\"Test message\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uid").value(1))
                .andExpect(jsonPath("$.reminderType").value("sms"))
                .andExpect(jsonPath("$.message").value("Test message"));

        verify(notificationService, times(1)).addNotification(any(Notification.class));
    }

    @Test
    void testGetAllNotifications() throws Exception {
        List<Notification> notifications = Arrays.asList(
                new Notification(1, "email", "Test message 1", LocalDateTime.now()),
                new Notification(2, "sms", "Test message 2", LocalDateTime.now())
        );
        when(notificationService.getAllNotifications()).thenReturn(notifications);

        mockMvc.perform(get("/notifications/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].uid").value(1))
                .andExpect(jsonPath("$[0].reminderType").value("email"))
                .andExpect(jsonPath("$[0].message").value("Test message 1"))
                .andExpect(jsonPath("$[1].uid").value(2))
                .andExpect(jsonPath("$[1].reminderType").value("sms"))
                .andExpect(jsonPath("$[1].message").value("Test message 2"));

        verify(notificationService, times(1)).getAllNotifications();
    }


@Test
    void testDeleteNotification() throws Exception {
        doNothing().when(notificationService).deleteNotification(1);

        mockMvc.perform(delete("/notifications/delete/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Notification with ID 1 has been deleted."));

        verify(notificationService, times(1)).deleteNotification(1);
    }
}

