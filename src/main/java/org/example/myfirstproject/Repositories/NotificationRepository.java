package org.example.myfirstproject.Repositories;

import org.example.myfirstproject.Models.Entities.Notification;
import org.example.myfirstproject.Models.Enums.NotificationStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepository extends JpaRepository <Notification, Long>{
    List<Notification> findByUserId(Long id);

    int countByUserIdAndStatus(Long id, NotificationStatusEnum status);

    List<Notification> findByUserIdAndStatus(Long userId, NotificationStatusEnum status);

    void deleteByUserId(Long id);
}
