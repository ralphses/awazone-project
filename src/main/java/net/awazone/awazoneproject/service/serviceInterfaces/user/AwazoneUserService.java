package net.awazone.awazoneproject.service.serviceInterfaces.user;

import net.awazone.awazoneproject.model.dtos.user.AwazoneUserAddressRequest;
import net.awazone.awazoneproject.model.dtos.user.AwazoneUserContactRequest;
import net.awazone.awazoneproject.model.dtos.user.NewRegistrationRequest;
import net.awazone.awazoneproject.model.user.UserToken;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUser;
import org.springframework.security.core.Authentication;

import java.security.Principal;
import java.util.List;
import java.util.Map;

public interface AwazoneUserService {

    List<AwazoneUser> getAllUsers(int page);
    public AwazoneUser registerNewUser(NewRegistrationRequest newRegistrationRequest);
    int countAllById();
    int countAllByAccountNonLocked();
    int countAllByEnabled();
    AwazoneUser findAppUserById(Long userId);
    AwazoneUser findAppUserByUsername(String username);
    AwazoneUser findAppUserByEmail(String email);
    AwazoneUser findAppUserByDomain(String domain);
    void updateAppUserContactDetails(Long userId, AwazoneUserContactRequest awazoneUserContactRequest, Principal principal);
    void updateAppUserDomainDetails(Long userId, String newDomainName, Principal principal);

    boolean isAccountActive(String username);

    void assignRoleToUser(String roleName, Long userId);

    void updateUserPassword(UserToken userToken, String newPassword);

    void updateUserPassword(AwazoneUser awazoneUser, String newPassword);

    void updateAppUserAddressDetails(Long userId, AwazoneUserAddressRequest awazoneUserAddressRequest, Principal principal);

    AwazoneUser findByReferralCode(String referralCoe);

    Map<String, List<AwazoneUser>> fetchUserReferralTree(String username, Authentication authentication);
}
