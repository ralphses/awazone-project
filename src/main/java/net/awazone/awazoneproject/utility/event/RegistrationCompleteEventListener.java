package net.awazone.awazoneproject.utility.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.service.serviceInterfaces.user.NotificationService;
import net.awazone.awazoneproject.service.serviceInterfaces.user.UserTokenService;
import net.awazone.awazoneproject.utility.templates.MessageTemplate;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static net.awazone.awazoneproject.model.user.TokenType.SIGN_UP;
import static net.awazone.awazoneproject.model.user.notification.NotificationPurpose.SIGN_UP_TOKEN;
import static net.awazone.awazoneproject.model.user.notification.NotificationType.EMAIL;
import static net.awazone.awazoneproject.model.user.notification.NotificationType.SMS;

@Slf4j
@Component
@RequiredArgsConstructor
public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {

    private final UserTokenService userTokenService;
    private final NotificationService notificationService;

    @Override
    @Transactional
    public void onApplicationEvent(RegistrationCompleteEvent registrationCompleteEvent) {

        AwazoneUser awazoneUser = registrationCompleteEvent.getAwazoneUser();

        String token = UUID.randomUUID().toString();
        String senderAddress = "no-reply@awazone.net";


        userTokenService.createNewToken(awazoneUser, token, SIGN_UP);

        String activationLink = registrationCompleteEvent.getUrlLink() + "/user/activate?token=" + token;
        String message = MessageTemplate.signUpEmail(awazoneUser.getFullName(), activationLink);

        //Send mail
        notificationService.sendNotification(awazoneUser, activationLink, EMAIL, SIGN_UP_TOKEN, senderAddress, "Activate your account", message);


    }
}
