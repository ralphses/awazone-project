package net.awazone.awazoneproject.service.serviceInterfaces.user;

import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.model.userService.notification.NotificationPurpose;
import net.awazone.awazoneproject.model.userService.notification.NotificationType;

@FunctionalInterface
public interface NotificationService {
    void sendNotification(AwazoneUser appUser, String activationLink, NotificationType notificationType, NotificationPurpose notificationPurpose, String sender, String subject, String message);
}
