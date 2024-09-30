package com.example.notificationService.controller;

import com.example.notificationService.model.Notification;
import com.example.notificationService.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/notifications")
@CrossOrigin(origins = {"http://localhost:3000", "http://localhost:3001", "*"})
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping("/send")
    public List<Notification> sendNotification(){
        return notificationService.findWithCurrentDate(LocalDateTime.now());
    }

    @PostMapping("/add")
    public Notification addNotification(@RequestBody Notification notification) {
        return notificationService.addNotification(notification);
    }

    @PutMapping("/notifications/update/{nid}")
    public ResponseEntity<Notification> updateNotification(@PathVariable Integer nid, @RequestBody Notification notification) {
        Notification updatedNotification = notificationService.updateNotification(nid, notification);
        if (updatedNotification == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedNotification);
    }


    @DeleteMapping("/delete/{nid}")
    public String deleteNotification(@PathVariable Integer nid) {
        notificationService.deleteNotification(nid);
        return "Notification with ID " + nid + " has been deleted.";
    }


    @GetMapping("/all")
    public List<Notification> getAllNotifications( Integer uid) {
        return notificationService.getAllNotifications();
    }

}