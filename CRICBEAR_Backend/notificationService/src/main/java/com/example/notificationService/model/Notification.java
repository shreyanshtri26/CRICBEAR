package com.example.notificationService.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "nid")
    private int nid;

    @Column(name = "uid")
    private int uid;


    @Column(name = "reminder_type")
    private String reminderType;

    @Column(name = "message")
    private String message;

    @Column(name = "sentAt")
    private LocalDateTime sentAt;


    // Constructors
    public Notification(Integer uid, String reminderType, String message, LocalDateTime now){
        this.uid = uid;
        this.reminderType = reminderType;
        this.message = message;
    }

}


