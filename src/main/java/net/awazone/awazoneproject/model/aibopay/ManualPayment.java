package net.awazone.awazoneproject.model.aibopay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.awazone.awazoneproject.model.aibopay.transaction.TransactionStatus;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ManualPayment {

    @Id
    @GeneratedValue(generator = "manual_pay_id_generator", strategy = SEQUENCE)
    @SequenceGenerator(name = "manual_pay_id_generator", sequenceName = "manual_pay_id_generator", allocationSize = 1)
    private Long id;

    private String reference;
    private String emailAddress;
    private String paymentPurpose;

    @Enumerated(STRING)
    private TransactionStatus transactionStatus;
    private String imageUrl;
    private LocalDateTime submittedOn;
}
