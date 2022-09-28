package net.awazone.awazoneproject.repository.user;

import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.model.userService.kyc.KycDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KycDocumentRepository extends JpaRepository<KycDocument, Long> {

    @Query(value = "SELECT kycDocument FROM KycDocument kycDocument WHERE kycDocument.awazoneUser = ?1")
    Optional<KycDocument> findByAwazoneUser(AwazoneUser awazoneUser);
}
