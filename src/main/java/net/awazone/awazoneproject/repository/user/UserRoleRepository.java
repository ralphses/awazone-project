package net.awazone.awazoneproject.repository.user;

import net.awazone.awazoneproject.model.user.awazoneUserRole.AwazoneUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<AwazoneUserRole, Long> {

    @Query("SELECT role FROM AwazoneUserRole role WHERE role.roleName = ?1")
    Optional<AwazoneUserRole> findByRoleName(String roleName);
}
