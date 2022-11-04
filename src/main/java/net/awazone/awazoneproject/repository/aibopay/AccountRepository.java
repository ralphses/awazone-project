package net.awazone.awazoneproject.repository.aibopay;

import net.awazone.awazoneproject.model.aibopay.account.AibopayAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<AibopayAccount, Long> {

    @Query(value = "SELECT account FROM AibopayAccount account WHERE account.accountNumber = ?1")
    Optional<AibopayAccount> findByAccountNumber(String accountNumber);

    @Query(value = "SELECT account FROM AibopayAccount account WHERE account.accountReference = ?1")
    Optional<AibopayAccount> findByAccountReference(String accountReference);
}
