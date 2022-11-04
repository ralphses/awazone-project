package net.awazone.awazoneproject.repository.aibopay;

import net.awazone.awazoneproject.model.aibopay.ManualPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManualPaymentRepository extends JpaRepository<ManualPayment, Long> {

    @Query(value = "SELECT payment FROM ManualPayment payment WHERE payment.reference = ?1")
    Optional<ManualPayment> findByReference(String reference);
}
