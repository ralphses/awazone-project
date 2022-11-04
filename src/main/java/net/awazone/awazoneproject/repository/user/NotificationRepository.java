package net.awazone.awazoneproject.repository.user;

import net.awazone.awazoneproject.model.user.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
