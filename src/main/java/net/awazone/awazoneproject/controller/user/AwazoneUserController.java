package net.awazone.awazoneproject.controller.user;

import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.model.dtos.user.AwazoneUserAddressRequest;
import net.awazone.awazoneproject.model.dtos.user.AwazoneUserContactRequest;
import net.awazone.awazoneproject.service.serviceInterfaces.user.AwazoneUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.security.Principal;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/user")
public class AwazoneUserController {

    private final AwazoneUserService awazoneUserService;

    @PreAuthorize("hasAnyAuthority('ROLE_VIEW_USERS', 'ROLE_SUPER:ADMIN')")
    @GetMapping(path = "/get/all/{page}")
    public ResponseEntity<ResponseMessage> getAllUsers(@PathVariable int page) {

        List<AwazoneUser> allUsers = awazoneUserService.getAllUsers(page);
        return ResponseEntity.ok(new ResponseMessage("success", OK, Map.of("users", allUsers)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_COUNT_USER_BY_ID', 'ROLE_SUPER:ADMIN')")
    @GetMapping(path = "/get/count-by-id")
    public ResponseEntity<ResponseMessage> countAllById(){

        int count = awazoneUserService.countAllById();
        return ResponseEntity.ok(new ResponseMessage("success", OK, Map.of("total", count)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_COUNT_UNLOCKED_USER', 'ROLE_SUPER:ADMIN')")
    @GetMapping(path = "/get/count-by-non-locked")
    public ResponseEntity<ResponseMessage> countAllByAccountNonLocked() {

        int count = awazoneUserService.countAllByAccountNonLocked();
        return ResponseEntity.ok(new ResponseMessage("success", OK, Map.of("total", count)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_COUNT_ENABLED_USER', 'ROLE_SUPER:ADMIN')")
    @GetMapping(path = "/get/count-by-enabled")
    public ResponseEntity<ResponseMessage> countAllByEnabled() {

        int count = awazoneUserService.countAllByEnabled();
        return ResponseEntity.ok(new ResponseMessage("success", OK, Map.of("total", count)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GET_USER_BY_ID', 'ROLE_SUPER:ADMIN', 'ROLE_USER')")
    @GetMapping(path = "/get/{userId}")
    ResponseEntity<ResponseMessage> findAppUserById(@PathVariable Long userId) {

        AwazoneUser user = awazoneUserService.findAppUserById(userId);
        return ResponseEntity.ok(new ResponseMessage("success", OK, Map.of("user", user)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GET_USER_BY_USERNAME', 'ROLE_SUPER:ADMIN', 'ROLE_USER')")
    @GetMapping(path = "/get/username")
    public ResponseEntity<ResponseMessage> findAppUserByUsername(@RequestParam(name = "username") String username) {

        AwazoneUser user = awazoneUserService.findAppUserByUsername(username);
        return ResponseEntity.ok(new ResponseMessage("success", OK, Map.of("user", user)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_GET_USER_BY_EMAIL', 'ROLE_SUPER:ADMIN', 'ROLE_USER')")
    @GetMapping(path = "/get/email")
    public ResponseEntity<ResponseMessage> findAppUserByEmail(@RequestParam(name = "email") String email) {

        AwazoneUser user = awazoneUserService.findAppUserByUsername(email);
        return ResponseEntity.ok(new ResponseMessage("success", OK, Map.of("user", user)));

    }

    @PreAuthorize("hasAnyAuthority('ROLE_GET_USER_BY_DOMAIN', 'ROLE_SUPER:ADMIN', 'ROLE_USER')")
    @GetMapping(path = "/get/domain")
    public ResponseEntity<ResponseMessage> findAppUserByDomain(@RequestParam(name = "domain") String domain) {

        AwazoneUser user = awazoneUserService.findAppUserByDomain(domain);
        return ResponseEntity.ok(new ResponseMessage("success", OK, Map.of("user", user)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_UPDATE_USER_CONTACT','ROLE_USER')")
    @PutMapping(path = "/update/contact/{userId}")
    public ResponseEntity<ResponseMessage> updateAppUserContactDetails(@PathVariable Long userId,
                                                                       @RequestBody @Valid AwazoneUserContactRequest awazoneUserContactRequest,
                                                                       Principal principal) {
        awazoneUserService.updateAppUserContactDetails(userId, awazoneUserContactRequest, principal);
        return ResponseEntity.ok(new ResponseMessage("User contact details updated successfully", OK, null));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_UPDATE_USER_ADDRESS','ROLE_USER')")
    @PutMapping(path = "/update/address/{userId}")
    public ResponseEntity<ResponseMessage> addAppUserAddressDetails(@PathVariable Long userId,
                                                                       @RequestBody @Valid AwazoneUserAddressRequest awazoneUserAddressRequest,
                                                                       Principal principal) {
        awazoneUserService.updateAppUserAddressDetails(userId, awazoneUserAddressRequest, principal);
        return ResponseEntity.ok(new ResponseMessage("User address details updated successfully", OK, null));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_UPDATE_USER_DOMAIN','ROLE_USER')")
    @PutMapping(path = "/update/domain/{userId}")
    public ResponseEntity<ResponseMessage> updateAppUserDomainDetails(@PathVariable Long userId,
                                                                      @RequestParam String domain,
                                                                      Principal principal) {
        awazoneUserService.updateAppUserDomainDetails(userId, domain, principal );
        return ResponseEntity.ok(new ResponseMessage("User domain updated successfully", OK, null));
    }

    @GetMapping(path = "/get/refer")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER:ADMIN')")
    public ResponseEntity<ResponseMessage> getUserByReferralCode(@RequestParam("referralCode") @NotBlank String referralCode) {
        AwazoneUser awazoneUser = awazoneUserService.findByReferralCode(referralCode);
        return ResponseEntity.ok(new ResponseMessage("success", OK, Map.of("user", awazoneUser)));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SUPER:ADMIN','ROLE_USER')")
    @GetMapping(path = "/get/referral-tree")
    public ResponseEntity<ResponseMessage> fetchUserReferralTree(@RequestParam("username") String username,
                                                                 Authentication authentication) {
        Map<String, List<AwazoneUser>> tree = awazoneUserService.fetchUserReferralTree(username, authentication );
        return ResponseEntity.ok(new ResponseMessage("User domain updated successfully", OK, Map.of("referralTree", tree)));
    }


}
