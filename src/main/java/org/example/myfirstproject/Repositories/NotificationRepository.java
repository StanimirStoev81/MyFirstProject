package org.example.myfirstproject.Repositories;

import org.example.myfirstproject.Models.Entities.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository <Notification, Long>{
}
