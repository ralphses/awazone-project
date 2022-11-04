package net.awazone.awazoneproject.utility.event;

import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.service.serviceInterfaces.user.UserTokenService;
import net.awazone.awazoneproject.service.serviceInterfaces.user.NotificationService;
import net.awazone.awazoneproject.utility.templates.MessageTemplate;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

import static net.awazone.awazoneproject.model.user.TokenType.PASSWORD_RESET;
import static net.awazone.awazoneproject.model.user.notification.NotificationPurpose.PASSWORD_RESET_TOKEN;
import static net.awazone.awazoneproject.model.user.notification.NotificationType.EMAIL;

@Component
@AllArgsConstructor
public class PasswordResetEventListener implements ApplicationListener<PasswordResetEvent> {

    private final NotificationService notificationService;
    private final UserTokenService userTokenService;

    @Override
    public void onApplicationEvent(PasswordResetEvent passwordResetEvent) {
        AwazoneUser awazoneUser = (AwazoneUser) passwordResetEvent.getSource();
        String rootLink = passwordResetEvent.getPasswordResetLink();
        String senderAddress = "no-reply@awazone.net";
        String token = UUID.randomUUID().toString();

        userTokenService.createNewToken(awazoneUser, token, PASSWORD_RESET);

        String resetLink = rootLink + "/user/password-reset?token=" + token;
        String message = MessageTemplate.passwordResetEmail(awazoneUser.getFullName(), resetLink);

        notificationService.sendNotification(awazoneUser, resetLink, EMAIL, PASSWORD_RESET_TOKEN, senderAddress, "Password Reset", message);
    }
}
