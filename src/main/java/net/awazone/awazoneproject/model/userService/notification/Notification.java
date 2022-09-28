package net.awazone.awazoneproject.model.userService.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Notification {

    @Id
    @SequenceGenerator(
            name = "notification_id_sequence",
            sequenceName = "notification_id_sequence"
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "notification_id_sequence"
    )
    private Long notificationId;
    private Long toCustomerId;
    private String toCustomerEmail;
    private String toCustomerPhone;

    @Enumerated(STRING)
    private NotificationType notificationType;
    private String sender;

    @Enumerated(STRING)
    private NotificationPurpose purpose;
    private LocalDateTime sentAt;

}
