package net.awazone.awazoneproject.controller.user;

import lombok.RequiredArgsConstructor;
import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.model.dtos.user.AwazoneUserAddressRequest;
import net.awazone.awazoneproject.model.dtos.user.PasswordResetModel;
import net.awazone.awazoneproject.service.serviceInterfaces.user.AuthService;
import net.awazone.awazoneproject.utility.validation.annotations.AdminSuper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Set;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/v1/admin/super")
public class SuperAdminController {

    private final AuthService authService;

    @PutMapping(path = "/activate")
    public ResponseEntity<ResponseMessage> activateAdmin(@RequestParam("username") @NotBlank String username,
                                                         @RequestParam("values") @NotBlank Set<String> phrases) {
        return ResponseEntity.ok(authService.activateUser(username, phrases));
    }

    @AdminSuper
    @PutMapping(path = "/update/password")
    public ResponseEntity<ResponseMessage> updatePassword(@RequestParam("username") String username,
                                                          @RequestParam("values") Set<String> phrases,
                                                          @RequestBody @Valid PasswordResetModel passwordResetModel) {
        return ResponseEntity.ok(authService.passwordReset(username, phrases, passwordResetModel));
    }

    @AdminSuper
    @PutMapping(path = "/update/fullName")
    public ResponseEntity<ResponseMessage> updateFullName(@RequestParam("username") String username,
                                                          @RequestParam("values") Set<String> phrases,
                                                          @RequestParam("newFullName") @NotBlank String newFullName) {
        return ResponseEntity.ok(authService.updateAdminName(username, phrases, newFullName));
    }

    @AdminSuper
    @PutMapping(path = "/update/mobilePhone")
    public ResponseEntity<ResponseMessage> updateMobilePhone(@RequestParam("username") String username,
                                                             @RequestParam("values") Set<String> phrases,
                                                             @RequestParam("newMobilePhone") @NotBlank String newMobilePhone) {
        return ResponseEntity.ok(authService.updateAdminPhone(username, phrases, newMobilePhone));
    }

    @AdminSuper
    @PutMapping(path = "/update/address")
    public ResponseEntity<ResponseMessage> updateAppUserAddressDetails(@RequestParam("username") @NotBlank String username,
                                                                       @RequestParam("values") @NotBlank Set<String> phrases,
                                                                       @RequestBody @Valid AwazoneUserAddressRequest awazoneUserAddressRequest) {
        return ResponseEntity.ok(authService.adminAddress(username, phrases, awazoneUserAddressRequest));
    }

    @AdminSuper
    @PostMapping(path = "/new")
    public ResponseEntity<ResponseMessage> createAdmin(@RequestParam("values") @NotBlank Set<String> phrases,
                                                                       @RequestParam("newAdminName") @NotBlank String newAdminUserName,
                                                                       @RequestParam("roleName") @NotBlank String roleName) {
        return ResponseEntity.ok(authService.addNewAdmin(phrases, newAdminUserName, roleName));
    }


}
