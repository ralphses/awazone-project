package net.awazone.awazoneproject.service.serviceInterfaces.user;

import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.model.user.notification.NotificationPurpose;
import net.awazone.awazoneproject.model.user.notification.NotificationType;

public interface NotificationService {
    void sendNotification(AwazoneUser appUser, String activationLink, NotificationType notificationType, NotificationPurpose notificationPurpose, String sender, String subject, String message);
    void sendNotification(String destination, NotificationType notificationType, NotificationPurpose notificationPurpose, String sender, String subject, String message);
}
