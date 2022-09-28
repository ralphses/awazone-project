package net.awazone.awazoneproject.service.serviceInterfaces.user;

import net.awazone.awazoneproject.model.requests.user.AwazoneUserAddressRequest;
import net.awazone.awazoneproject.model.requests.user.AwazoneUserContactRequest;
import net.awazone.awazoneproject.model.requests.user.NewRegistrationRequest;
import net.awazone.awazoneproject.model.userService.UserToken;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;

import java.util.List;

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
    void updateAppUserContactDetails(Long userId, AwazoneUserContactRequest awazoneUserContactRequest);
    void updateAppUserAddressDetails(Long userId, AwazoneUserAddressRequest awazoneUserAddressRequest);
    void updateAppUserDomainDetails(Long userId, String newDomainName);

    boolean isAccountActive(String username);

    void assignRoleToUser(String roleName, Long userId);
    void removeRoleFromUser(String roleName, Long userId);

    void updateUserPassword(UserToken userToken, String newPassword);

    void updateUserPassword(AwazoneUser awazoneUser, String newPassword);
}
