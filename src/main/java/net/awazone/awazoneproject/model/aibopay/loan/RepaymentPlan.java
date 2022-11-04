package net.awazone.awazoneproject.model.aibopay.loan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RepaymentPlan {

    @Id
    private Long id;
    private PlanType planType;
    private LocalDateTime nextPaymentDate;
    private LocalDateTime previousPaymentDate;
}
