package net.awazone.awazoneproject.repository.user;

import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AwazoneUserRepository extends JpaRepository<AwazoneUser, Long> {

    @Query(value = "SELECT awazoneUser FROM AwazoneUser awazoneUser WHERE awazoneUser.awazoneUserDomain.domainName = ?1")
    Optional<AwazoneUser> findByUserDomainName(String domainName);

    @Query(value = "SELECT awazoneUser FROM AwazoneUser awazoneUser WHERE awazoneUser.awazoneUserContact.email = ?1")
    Optional<AwazoneUser> findByAwazoneUserContactEmail(String email);

    @Query(value = "SELECT awazoneUser FROM AwazoneUser awazoneUser WHERE awazoneUser.awazoneUserContact.mobilePhone = ?1 OR awazoneUser.awazoneUserContact.otherPhone = ?1")
    Optional<AwazoneUser> findByAAndAwazoneUserContactMobilePhoneOrAwazoneUserContactOtherPhone(String phoneNumber);

    @Query(value = "SELECT count(id) FROM awazone_user;", nativeQuery = true)
    int countAllUsers();

    @Query(nativeQuery = true, value = "SELECT count(id) FROM awazone_user WHERE is_account_non_locked = true")
    int countAllByUnlocked();

    @Query(nativeQuery = true, value = "SELECT count(id) FROM awazone_user WHERE is_enabled = true")
    int countAllEnabled();


}
