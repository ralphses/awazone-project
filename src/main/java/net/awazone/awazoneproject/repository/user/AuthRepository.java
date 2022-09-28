package net.awazone.awazoneproject.repository.user;

import net.awazone.awazoneproject.model.userService.Auth;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<Auth, Long> {

    @Query("SELECT auth FROM Auth auth WHERE auth.token = ?1")
    Optional<Auth> findByToken(String token);

    @Query("SELECT auths FROM Auth auths WHERE auths.awazoneUser = ?1")
    List<Auth> findAllByAppUser(AwazoneUser awazoneUser);

    @Query("SELECT auth FROM Auth auth WHERE auth.awazoneUser = ?1")
    Optional<Auth> findByAppUser(AwazoneUser awazoneUser);
}
