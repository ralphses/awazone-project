package net.awazone.awazoneproject.controller.user;

import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.controller.exception.ResponseMessage;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.model.requests.user.AwazoneUserAddressRequest;
import net.awazone.awazoneproject.model.requests.user.AwazoneUserContactRequest;
import net.awazone.awazoneproject.service.serviceInterfaces.user.AwazoneUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/user")
public class AwazoneUserController {

    private final AwazoneUserService awazoneUserService;

    @PreAuthorize("hasAnyAuthority('ROLE_VIEW_USERS', 'ROLE_SUPER:ADMIN')")
    @GetMapping(path = "/get/all/{page}")
    List<AwazoneUser> getAllUsers(@PathVariable int page) {
        return awazoneUserService.getAllUsers(page);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_COUNT_USER_BY_ID', 'ROLE_SUPER:ADMIN')")
    @GetMapping(path = "/get/count-by-id")
    int countAllById(){
        return awazoneUserService.countAllById();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_COUNT_UNLOCKED_USER', 'ROLE_SUPER:ADMIN')")
    @GetMapping(path = "/get/count-by-non-locked")
    int countAllByAccountNonLocked() {
        return awazoneUserService.countAllByAccountNonLocked();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_COUNT_ENABLED_USER', 'ROLE_SUPER:ADMIN')")
    @GetMapping(path = "/get/count-by-enabled")
    int countAllByEnabled() {
        return awazoneUserService.countAllByEnabled();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GET_USER_BY_ID', 'ROLE_SUPER:ADMIN', 'ROLE_USER')")
    @GetMapping(path = "/get/{userId}")
    AwazoneUser findAppUserById(@PathVariable Long userId) {
        return awazoneUserService.findAppUserById(userId);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GET_USER_BY_USERNAME', 'ROLE_SUPER:ADMIN', 'ROLE_USER')")
    @GetMapping(path = "/get/username")
    AwazoneUser findAppUserByUsername(@RequestParam(name = "username") String username) {
        return awazoneUserService.findAppUserByUsername(username);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GET_USER_BY_EMAIL', 'ROLE_SUPER:ADMIN', 'ROLE_USER')")
    @GetMapping(path = "/get/email")
    AwazoneUser findAppUserByEmail(@RequestParam(name = "email") String email) {
        return awazoneUserService.findAppUserByUsername(email);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GET_USER_BY_DOMAIN', 'ROLE_SUPER:ADMIN', 'ROLE_USER')")
    @GetMapping(path = "/get/domain")
    AwazoneUser findAppUserByDomain(@RequestParam(name = "domain") String domain) {
        return awazoneUserService.findAppUserByDomain(domain);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_UPDATE_USER_CONTACT','ROLE_USER')")
    @PutMapping(path = "/update/contact/{userId}")
    public ResponseEntity<ResponseMessage> updateAppUserContactDetails(@PathVariable Long userId,
                                                                       @RequestBody @Valid AwazoneUserContactRequest awazoneUserContactRequest) {
        awazoneUserService.updateAppUserContactDetails(userId, awazoneUserContactRequest);
        return ResponseEntity.ok(new ResponseMessage("User contact details updated successfully", OK));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_UPDATE_USER_ADDRESS','ROLE_USER')")
    @PutMapping(path = "/update/address/{userId}")
    public ResponseEntity<ResponseMessage> updateAppUserAddressDetails(@PathVariable Long userId,
                                                                       @RequestBody @Valid AwazoneUserAddressRequest awazoneUserAddressRequest) {
        awazoneUserService.updateAppUserAddressDetails(userId, awazoneUserAddressRequest);
        return ResponseEntity.ok(new ResponseMessage("User address details updated successfully", OK));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_UPDATE_USER_DOMAIN','ROLE_USER')")
    @PutMapping(path = "/update/domain/{userId}")
    public ResponseEntity<ResponseMessage> updateAppUserDomainDetails(@PathVariable Long userId,
                                                                      @RequestParam String domain) {
        awazoneUserService.updateAppUserDomainDetails(userId, domain);
        return ResponseEntity.ok(new ResponseMessage("User domain updated successfully", OK));
    }

}
