package net.awazone.awazoneproject.model.aibopay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.awazone.awazoneproject.model.aibopay.account.AibopayAccount;
import net.awazone.awazoneproject.model.aibopay.loan.Loan;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUser;

import javax.persistence.*;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.SEQUENCE;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserWallet {

    @Id
    @GeneratedValue(generator = "wallet_id_generator", strategy = SEQUENCE)
    @SequenceGenerator(name = "wallet_id_generator", sequenceName = "wallet_id_generator", allocationSize = 1)
    private Long id;

    private String address;

    @OneToOne(fetch = EAGER, cascade = DETACH)
    private AwazoneUser awazoneUser;

    @OneToOne(fetch = EAGER, cascade = DETACH)
    private AibopayAccount account;

    @OneToOne(fetch = EAGER, cascade = DETACH)
    private Loan loan;

}
