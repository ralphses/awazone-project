package net.awazone.awazoneproject.repository.aibopay;

import net.awazone.awazoneproject.model.aibopay.UserWallet;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserWalletRepository extends JpaRepository<UserWallet, Long> {

    @Query(value = "SELECT wallet FROM UserWallet wallet WHERE wallet.address = ?1")
    Optional<UserWallet> findByAddress(String address);

    @Query(value = "SELECT wallet FROM UserWallet wallet WHERE wallet.awazoneUser = ?1")
    Optional<UserWallet> findByAwazoneUser(AwazoneUser awazoneUser);

    @Query(value = "SELECT wallet FROM UserWallet wallet WHERE wallet.account.accountNumber = ?1")
    Optional<UserWallet> findByAccountAccountNumber(String accountNumber);

}
