package net.awazone.awazoneproject.controller.user;

import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.model.user.awazoneUserRole.AwazoneUserRole;
import net.awazone.awazoneproject.model.dtos.user.NewRoleRequest;
import net.awazone.awazoneproject.service.serviceInterfaces.user.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping(path = "/api/v1/user/role")
public class UserRoleController {

    @Autowired
    private UserRoleService userRoleService;

    @PostMapping(path = "/create")
    @Secured({"ROLE_SUPER:ADMIN", "ROLE_CREATE_ROLE"})
    public ResponseEntity<ResponseMessage> createNewRole(@RequestBody @Valid NewRoleRequest newRoleRequest) {
        userRoleService.createRole(newRoleRequest);
        return ResponseEntity.ok(new ResponseMessage("Role added successfully", OK, null));
    }

    @Secured({"ROLE_SUPER:ADMIN", "ROLE_UPDATE_ROLE_DESC"})
    @PutMapping(path = "/update/description")
    public ResponseEntity<ResponseMessage> updateRoleDescription(String roleName, String newDescription) {
        userRoleService.updateRoleDescription(roleName, newDescription);
        return ResponseEntity.ok(new ResponseMessage("Role description updated successfully", OK, null));
    }

    @Secured({"ROLE_SUPER:ADMIN", "ROLE_UPDATE_ROLE_NAME"})
    @PutMapping(path = "/update/name")
    public ResponseEntity<ResponseMessage> updateRoleName(String oldRoleName, String newRoleName) {
        userRoleService.updateRoleName(oldRoleName, newRoleName);
        return ResponseEntity.ok(new ResponseMessage("Role name updated successfully", OK, null));
    }

    @Secured({"ROLE_SUPER:ADMIN", "ROLE_DELETE_ROLE"})
    @DeleteMapping(path = "/delete/{roleId}")
    public ResponseEntity<ResponseMessage> deleteRole(@PathVariable Long roleId) {
        userRoleService.deleteRole(roleId);
        return ResponseEntity.ok(new ResponseMessage("Role deleted successfully", OK, null));
    }

    @Secured({"ROLE_VIEW_ROLES", "ROLE_SUPER:ADMIN"})
    @GetMapping(path = "/get/roles/{page}")
    public List<AwazoneUserRole> getRoles(@PathVariable int page) {
        return userRoleService.allRoles(page);
    }

    @Secured({"ROLE_VIEW_AUTHORITIES", "ROLE_SUPER:ADMIN"})
    @GetMapping(path = "/get/authority")
    public List<String> getAuthorities() {
        return userRoleService.getAllAuthorities();
    }
}
