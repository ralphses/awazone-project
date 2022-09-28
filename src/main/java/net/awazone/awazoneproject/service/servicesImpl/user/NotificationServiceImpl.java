package net.awazone.awazoneproject.service.servicesImpl.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.awazone.awazoneproject.controller.exception.EmailNotSentException;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.model.userService.notification.Notification;
import net.awazone.awazoneproject.model.userService.notification.NotificationPurpose;
import net.awazone.awazoneproject.model.userService.notification.NotificationType;
import net.awazone.awazoneproject.repository.user.NotificationRepository;
import net.awazone.awazoneproject.service.serviceInterfaces.user.NotificationService;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import java.time.LocalDateTime;

import static net.awazone.awazoneproject.controller.exception.ResponseMessage.EMAIL_NOT_SENT;
import static net.awazone.awazoneproject.model.userService.notification.NotificationType.EMAIL;
import static net.awazone.awazoneproject.model.userService.notification.NotificationType.SMS;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final JavaMailSender mailSender;

    @Override
    @Transactional
    @Async
    public void sendNotification(AwazoneUser awazoneUser, String activationLink, NotificationType notificationType, NotificationPurpose notificationPurpose, String sender, String subject, String message) {
        createNotification(awazoneUser.getAwazoneUserContact().getMobilePhone(), awazoneUser.getAwazoneUserContact().getEmail(), notificationType, notificationPurpose, sender);

        if(notificationType.equals(SMS)) {
            sendSms(awazoneUser.getAwazoneUserContact().getMobilePhone(), message, sender, subject);
        }
        else if(notificationType.equals(EMAIL)) {
            sendEmail(awazoneUser.getAwazoneUserContact().getEmail(), message, sender, subject);
        }
    }

    private void sendEmail(String toEmail, String message, String sender, String subject) {

        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, "utf-8");
            mimeMessageHelper.setText(message, true);
            mimeMessageHelper.setFrom(sender);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setTo(toEmail);

            mailSender.send(mimeMessage);

        }catch (MessagingException | MailSendException messagingException) {
            log.error("Failed to send email", messagingException);
            throw new EmailNotSentException(EMAIL_NOT_SENT);
        }

    }

    private void sendSms(String mobilePhone, String message, String sender, String subject) {

    }


    private void createNotification(String toPhone, String toEmail, NotificationType notificationType, NotificationPurpose notificationPurpose, String sender) {

        Notification notification = Notification.builder()
                .notificationType(notificationType)
                .sentAt(LocalDateTime.now())
                .sender(sender)
                .toCustomerEmail(toEmail)
                .toCustomerPhone(toPhone)
                .purpose(notificationPurpose)
                .build();
        notificationRepository.save(notification);
    }
}
