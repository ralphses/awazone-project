package net.awazone.awazoneproject.model.aibopay.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AibopayAccount {

    @Id
    @GeneratedValue(generator = "account_id_generator", strategy = SEQUENCE)
    @SequenceGenerator(name = "account_id_generator", sequenceName = "account_id_generator", allocationSize = 1)
    private Long id;

    @Enumerated(STRING)
    private AccountType accountType;

    @Enumerated(STRING)
    private AccountStatus accountStatus;

    @Column(length = 11)
    private String bvn;

    private String customerName;
    private String accountReference;
    private LocalDateTime createdAt;
    private LocalDateTime activatedAt;
    private BigDecimal currentBalance;
    private String accountNumber;

    public void topFund(double amount) {
        setCurrentBalance(BigDecimal.valueOf(this.currentBalance.doubleValue() + amount));
    }
    public void lessFund(double amount) {
        setCurrentBalance(BigDecimal.valueOf(this.currentBalance.doubleValue() - amount));
    }
}
