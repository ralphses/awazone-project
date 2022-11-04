package net.awazone.awazoneproject.service.serviceInterfaces.user;

import net.awazone.awazoneproject.model.user.awazoneUserRole.AwazoneUserRole;
import net.awazone.awazoneproject.model.dtos.user.NewRoleRequest;

import java.util.List;

public interface UserRoleService {

    void createRole(NewRoleRequest newRoleRequest);
    void updateRoleDescription(String roleName, String newDescription);
    void updateRoleName(String oldRoleName, String newRoleName) ;
    void deleteRole(Long roleId);
    AwazoneUserRole getRoleByName(String roleName);
    List<String> getAllAuthorities();
    List<AwazoneUserRole> allRoles(int page);
}
