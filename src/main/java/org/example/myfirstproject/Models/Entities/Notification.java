package org.example.myfirstproject.Models.Entities;

import jakarta.persistence.*;
import org.example.myfirstproject.Models.Enums.NotificationStatusEnum;

@Entity
@Table(name = "notifications")
public class Notification extends BaseEntity {

    @Column(nullable = false)
    private String message; // Съобщение

    @Enumerated(EnumType.STRING)
    private NotificationStatusEnum status; // SENT, READ

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Кой получава известието

    public Notification() {
    }

    public Notification(String message, NotificationStatusEnum status, User user) {
        this.message = message;
        this.status = status;
        this.user = user;
    }

    public String getMessage() {
        return message;
    }

    public NotificationStatusEnum getStatus() {
        return status;
    }

    public User getUser() {
        return user;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(NotificationStatusEnum status) {
        this.status = status;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
