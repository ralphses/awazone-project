package net.awazone.awazoneproject.repository.user;

import net.awazone.awazoneproject.model.userService.JwtToken;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

    @Query("SELECT jwt FROM JwtToken jwt WHERE jwt.jwtTokenString = ?1")
    Optional<JwtToken> findByToken(String token);

    @Query("SELECT jwt FROM JwtToken jwt WHERE jwt.awazoneUser = ?1")
    List<JwtToken> findAllByAppUser(AwazoneUser awazoneUser);

    @Query("SELECT jwt FROM JwtToken jwt WHERE jwt.awazoneUser = ?1")
    Optional<JwtToken> findByAppUser(AwazoneUser awazoneUser);

    @Query("SELECT jwtToken FROM JwtToken jwtToken WHERE jwtToken.awazoneUser.awazoneUserContact.email = ?1")
    List<JwtToken> findByAwazoneUserAwazoneUserContactEmail(String email);
    @Query("SELECT jwtToken FROM JwtToken jwtToken WHERE jwtToken.remoteAddress = ?1")
    List<JwtToken> findByRemoteAddress(String address);
}
