package net.awazone.awazoneproject.model.aibopay.transaction;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.awazone.awazoneproject.model.aibopay.UserWallet;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AibopayTransaction {

    @Id
    @GeneratedValue(generator = "transaction_id_generator", strategy = SEQUENCE)
    @SequenceGenerator(name = "transaction_id_generator", sequenceName = "transaction_id_generator", allocationSize = 1)
    private Long id;

    @Enumerated(STRING)
    private TransactionType transactionType;

    @Column(length = 300)
    private String beneficiary;

    @Enumerated(STRING)
    private TransactionStatus transactionStatus;

    @ManyToOne(fetch = EAGER, cascade = DETACH)
    private UserWallet userWallet;

    private String paymentMethod;
    private String description;
    private String transactionId;
    private LocalDateTime createdAt;
    private BigDecimal amount;
    private BigDecimal transactionFees;

}
