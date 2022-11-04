package net.awazone.awazoneproject.repository.aibopay;

import net.awazone.awazoneproject.model.aibopay.UserWallet;
import net.awazone.awazoneproject.model.aibopay.transaction.AibopayTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<AibopayTransaction, Long> {

    @Query(value = "SELECT trans FROM AibopayTransaction trans WHERE trans.transactionId = ?1")
    Optional<AibopayTransaction> findByTransactionId(String transactionID);

    @Query(value = "SELECT trans FROM AibopayTransaction trans WHERE trans.userWallet = ?1")
    Page<AibopayTransaction> findAllByUserWallet(UserWallet userWallet, Pageable pageable);
}
