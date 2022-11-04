package net.awazone.awazoneproject.model.aibopay.loan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import java.math.BigDecimal;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Loan {

    @Id
    @GeneratedValue(generator = "loan_id_generator", strategy = SEQUENCE)
    @SequenceGenerator(name = "loan_id_generator", sequenceName = "loan_id_generator", allocationSize = 1)
    private Long id;

    private BigDecimal loanAmount;
    @OneToOne(fetch = EAGER)
    private RepaymentPlan repaymentPlan;
    private BigDecimal currentBalance;

}
