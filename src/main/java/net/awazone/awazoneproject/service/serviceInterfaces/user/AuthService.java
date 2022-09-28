package net.awazone.awazoneproject.service.serviceInterfaces.user;

import net.awazone.awazoneproject.controller.exception.ResponseMessage;
import net.awazone.awazoneproject.model.userService.JwtToken;
import net.awazone.awazoneproject.model.userService.UserToken;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.model.requests.user.AwazoneUserAddressRequest;
import net.awazone.awazoneproject.model.requests.user.NewRegistrationRequest;
import net.awazone.awazoneproject.model.requests.user.PasswordResetModel;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface AuthService {

    AwazoneUser register(NewRegistrationRequest newRegistrationRequest);

    JwtToken saveAuthToken(String token);
    void passwordReset(String tokenString, PasswordResetModel passwordResetModel);

    UserToken activateUser(String token);

    Optional<JwtToken> findToken(String tokenString);

    List<JwtToken> findByAddress(String address);

    void resendLink(String userEmail, HttpServletRequest httpServletRequest);

    void saveAllJwts(List<JwtToken> jwtTokens);

    boolean userLoggedIn(String username);

    void getNewToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse);

    AwazoneUser findUserByEmail(String email);

    boolean isUserActivated(String username);


    ResponseMessage activateUser(String username, Set<String> phrases);

    ResponseMessage passwordReset(String username, Set<String> phrases, PasswordResetModel passwordResetModel);

    ResponseMessage updateAdminName(String username, Set<String> phrases, String newFullName);

    ResponseMessage updateAdminPhone(String username, Set<String> phrases, String newMobilePhone);

    ResponseMessage adminAddress(String username, Set<String> phrases, AwazoneUserAddressRequest awazoneUserAddressRequest);


    ResponseMessage addNewAdmin(Set<String> phrases, String newAdminUserName, String roleName);
}
