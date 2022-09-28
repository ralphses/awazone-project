package net.awazone.awazoneproject.model.aibopay.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;

import javax.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.SEQUENCE;
import static javax.persistence.TemporalType.TIME;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(generator = "account_id_generator", strategy = SEQUENCE)
    @SequenceGenerator(name = "account_id_generator", sequenceName = "account_id_generator", allocationSize = 1)
    private Long id;

    @Enumerated(STRING)
    private AccountType accountType;

    @Enumerated(STRING)
    private AccountStatus accountStatus;

    private String customerName;

    private LocalDateTime createdAt;

    private LocalDateTime activatedAt;

    private BigDecimal currentBalance;
    private Long accountNumber;

}
