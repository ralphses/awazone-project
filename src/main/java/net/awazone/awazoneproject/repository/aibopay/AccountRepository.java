package net.awazone.awazoneproject.repository.aibopay;

import net.awazone.awazoneproject.model.aibopay.account.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(value = "SELECT account FROM Account account WHERE account.accountNumber = ?1")
    Optional<Account> findByAccountNumber(Long accountNumber);
}
