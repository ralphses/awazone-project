package net.awazone.awazoneproject.repository.user;

import net.awazone.awazoneproject.model.user.TokenType;
import net.awazone.awazoneproject.model.user.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {

    @Query(value = "SELECT userToken FROM UserToken userToken WHERE userToken.awazoneUser.awazoneUserContact.email = ?1 AND userToken.tokenType = ?2")
    Optional<UserToken> findByAwazoneUserAwazoneUserContactEmailAndTokenType(String userEmail, TokenType tokenType);

    @Query(value = "SELECT userToken FROM UserToken userToken WHERE userToken.tokenString = ?1 AND userToken.tokenType = ?2")
    Optional<UserToken> findByTokenStringAAndTokenType(String tokenString, TokenType tokenType);
}
