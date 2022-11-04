package net.awazone.awazoneproject.service.servicesImpl.user;

import net.awazone.awazoneproject.exception.CustomInvalidParamException;
import net.awazone.awazoneproject.exception.IllegalUserException;
import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.model.user.ActivateAdminPhrase;
import net.awazone.awazoneproject.model.user.JwtToken;
import net.awazone.awazoneproject.model.user.UserToken;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUserAddress;
import net.awazone.awazoneproject.model.user.awazoneUserRole.AwazoneUserRole;
import net.awazone.awazoneproject.model.dtos.user.AwazoneUserAddressRequest;
import net.awazone.awazoneproject.model.dtos.user.NewRegistrationRequest;
import net.awazone.awazoneproject.model.dtos.user.PasswordResetModel;
import net.awazone.awazoneproject.repository.user.AuthRepository;
import net.awazone.awazoneproject.repository.user.JwtTokenRepository;
import net.awazone.awazoneproject.service.serviceInterfaces.user.*;
import net.awazone.awazoneproject.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;
import java.util.stream.Collectors;

import static com.google.common.net.HttpHeaders.REFRESH;
import static java.util.Arrays.stream;
import static net.awazone.awazoneproject.exception.ResponseMessage.ILLEGAL_USER;
import static net.awazone.awazoneproject.exception.ResponseMessage.PASSWORD_NOT_MATCH;
import static net.awazone.awazoneproject.model.user.TokenType.PASSWORD_RESET;
import static net.awazone.awazoneproject.model.user.TokenType.SIGN_UP;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
public class AuthServiceImpl implements AuthService {

    @Autowired
    private final AuthRepository authRepository;

    @Autowired
    private JwtConfig jwtConfig;
    private final JwtTokenRepository jwtTokenRepository;
    private final AwazoneUserService awazoneUserService;

    private final UserTokenService userTokenService;
    private final UserRoleService userRoleService;
    private final NotificationService notificationService;

    @Autowired
    private Utility utility;

    public static AuthServiceImpl authService;

    public AuthServiceImpl(AuthRepository authRepository, JwtTokenRepository jwtTokenRepository, AwazoneUserService awazoneUserService, UserTokenService userTokenService, UserRoleService userRoleService, NotificationService notificationService) {
        this.authRepository = authRepository;
        this.jwtTokenRepository = jwtTokenRepository;
        this.awazoneUserService = awazoneUserService;
        this.userTokenService = userTokenService;
        this.userRoleService = userRoleService;
        this.notificationService = notificationService;

        authService = this;
    }

    @Override
    public AwazoneUser register(NewRegistrationRequest newRegistrationRequest) {
        return awazoneUserService.registerNewUser(newRegistrationRequest);
    }

    @Override
    public void passwordReset(String tokenString, PasswordResetModel passwordResetModel) {

        UserToken userToken = userTokenService.verifyToken(tokenString, PASSWORD_RESET);

        if(!Objects.equals(passwordResetModel.getConfirmPassword(), passwordResetModel.getConfirmPassword()))
            throw new CustomInvalidParamException(PASSWORD_NOT_MATCH);

        awazoneUserService.updateUserPassword(userToken, passwordResetModel.getNewPassword());
    }

    public AwazoneUser findByEmail(String email) {
        return awazoneUserService.findAppUserByEmail(email);
    }

    @Override
    public JwtToken saveAuthToken(String token) {
        return null;
    }

    @Override
    public void activateUser(String token) {
        userTokenService.verifyToken(token, SIGN_UP);
    }

    @Override
    public Optional<JwtToken> findToken(String tokenString) {
        return jwtTokenRepository.findByToken(tokenString);
    }

    @Override
    public List<JwtToken> findByAddress(String address) {
        return jwtTokenRepository.findByRemoteAddress(address);
    }

    @Override
    public void saveAllJwts(List<JwtToken> jwtTokens) {
        jwtTokenRepository.saveAll(jwtTokens);
    }

    @Override
    public boolean userLoggedIn(String username) {
        List<JwtToken> jwtToken = jwtTokenRepository.findByAwazoneUserAwazoneUserContactEmail(username);
        Boolean isLoggedIn = jwtToken.stream().map(JwtToken::getLoggedIn).reduce(true, (first, second) -> first = second);
        return !jwtToken.isEmpty() && isLoggedIn;
    }

    @Override
    public void getNewToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {

        Optional<String> refreshTokenOptional = Optional.ofNullable(httpServletRequest.getHeader(REFRESH));

        if(refreshTokenOptional.isPresent()) {
            String refreshToken = refreshTokenOptional.get();
            jwtConfig.createNewTokens(refreshToken);
        }
    }

    @Override
    public AwazoneUser findUserByEmail(String email) {
        return awazoneUserService.findAppUserByEmail(email);
    }

    @Override
    public boolean isUserActivated(String username) {
        return awazoneUserService.isAccountActive(username);
    }

    @Override
    public ResponseMessage activateUser(String username, Set<String> phrases) {

        AwazoneUser awazoneUser = awazoneUserService.findAppUserByUsername(username);

        if(isPhraseValid(phrases)) {
            awazoneUser.setAccountNonLocked(true);
            awazoneUser.setAccountNonExpired(true);
            awazoneUser.setEnabled(true);

            return new ResponseMessage("Account activated successfully", OK, null);
        }
        throw new IllegalUserException(ILLEGAL_USER);
    }

    @Override
    public ResponseMessage passwordReset(String username, Set<String> phrases, PasswordResetModel passwordResetModel) {
        if(isPhraseValid(phrases) && Objects.equals(passwordResetModel.getNewPassword(), passwordResetModel.getConfirmPassword())) {
            AwazoneUser awazoneUser = awazoneUserService.findAppUserByUsername(username);
            awazoneUserService.updateUserPassword(awazoneUser, passwordResetModel.getNewPassword());

            return new ResponseMessage("Password updated successfully", OK, null);
        }
        throw new IllegalUserException(ILLEGAL_USER);
    }

    @Override
    public ResponseMessage updateAdminName(String username, Set<String> phrases, String newFullName) {
        if(isPhraseValid(phrases)) {
            AwazoneUser awazoneUser = awazoneUserService.findAppUserByUsername(username);
            awazoneUser.setFullName(newFullName);

            return new ResponseMessage("Full name updated successfully", OK, null);
        }
        throw new IllegalUserException(ILLEGAL_USER);

    }

    @Override
    public ResponseMessage updateAdminPhone(String username, Set<String> phrases, String newMobilePhone) {
        if(isPhraseValid(phrases)) {
            AwazoneUser awazoneUser = awazoneUserService.findAppUserByUsername(username);
            awazoneUser.getAwazoneUserContact().setMobilePhone(newMobilePhone);

            return new ResponseMessage("Mobile phone number updated successfully", OK, null);
        }
        throw new IllegalUserException(ILLEGAL_USER);
    }

    @Override
    public ResponseMessage adminAddress(String username, Set<String> phrases, AwazoneUserAddressRequest awazoneUserAddressRequest) {
        if (isPhraseValid(phrases)) {
            AwazoneUser awazoneUser = awazoneUserService.findAppUserByUsername(username);
            AwazoneUserAddress awazoneUserAddress = AwazoneUserAddress.builder()
                    .houseAddress(awazoneUserAddressRequest.getHomeAddress())
                    .street(awazoneUserAddressRequest.getStreet())
                    .province(awazoneUserAddressRequest.getProvince())
                    .country(awazoneUserAddressRequest.getCountry())
                    .build();
            awazoneUser.setAwazoneUserAddress(awazoneUserAddress);

            return new ResponseMessage("Address updated successfully", OK, null);

        }
        throw new IllegalUserException(ILLEGAL_USER);
    }

    @Override
    public ResponseMessage addNewAdmin(Set<String> phrases, String newAdminUserName, String roleName) {

        AwazoneUserRole awazoneUserRole = userRoleService.getRoleByName(roleName);
        AwazoneUser awazoneUser = awazoneUserService.findAppUserByUsername(newAdminUserName);
        if(isPhraseValid(phrases) && isUserActivated(awazoneUser.getAwazoneUserContact().getEmail())) {
            awazoneUser.setAwazoneUserRole(awazoneUserRole);
        }

        return new ResponseMessage("New admin with role " + roleName + " created!", OK, null);
    }


    private boolean isPhraseValid(Set<String> phrases) {
        return stream(ActivateAdminPhrase.values())
                .map(Enum::name)
                .collect(Collectors.toSet()).equals(phrases);
    }

}
