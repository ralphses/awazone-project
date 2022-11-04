package net.awazone.awazoneproject.controller.user;

import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.model.dtos.user.NewRegistrationRequest;
import net.awazone.awazoneproject.model.dtos.user.PasswordResetModel;
import net.awazone.awazoneproject.service.serviceInterfaces.user.AuthService;
import net.awazone.awazoneproject.utility.Utility;
import net.awazone.awazoneproject.utility.event.PasswordResetEvent;
import net.awazone.awazoneproject.utility.event.RegistrationCompleteEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import static net.awazone.awazoneproject.exception.ResponseMessage.*;
import static org.springframework.http.HttpStatus.OK;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/user")
public class AuthenticationController {

    private final ApplicationEventPublisher applicationEventPublisher;

    private final Utility utility;
    private final AuthService authService;

    @PostMapping(path = "/register")
    public AwazoneUser register(@RequestBody @Valid NewRegistrationRequest newRegistrationRequest,
                                HttpServletRequest httpServletRequest) {
        AwazoneUser awazoneUser = authService.register(newRegistrationRequest);

        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(awazoneUser, utility.applicationUrl(httpServletRequest)));
        return awazoneUser;
    }

    @PostMapping(path = "/activate")
    public ResponseEntity<ResponseMessage> activateUser(@RequestParam(name = "token", required = false) String token) {
        authService.activateUser(token);
        return ResponseEntity.ok(new ResponseMessage(USER_VERIFIED_SUCCESSFULLY, OK, null));
    }

    @PostMapping(path = "/refresh")
    public ResponseEntity<ResponseMessage> getNewToken(HttpServletRequest httpServletRequest,
                            HttpServletResponse httpServletResponse) {
        authService.getNewToken(httpServletRequest, httpServletResponse);
        return ResponseEntity.ok(new ResponseMessage("success", OK, null));
    }

    @PostMapping(path = "/reactivate-link")
    public ResponseEntity<ResponseMessage> reactivateLink(@RequestParam(name = "email") String email,
                                                          HttpServletRequest httpServletRequest) {
        AwazoneUser awazoneUser = authService.findUserByEmail(email);
        applicationEventPublisher.publishEvent(new RegistrationCompleteEvent(awazoneUser, utility.applicationUrl(httpServletRequest)));
        return ResponseEntity.status(OK).body(new ResponseMessage("Activation link re-sent", OK, null));
    }

    @GetMapping(path = "/password-reset")
    public ResponseEntity<ResponseMessage> passwordResetRequest(@RequestParam(name = "email") String email,
                                                                HttpServletRequest httpServletRequest) {
        //TODO: Send a password reset link to this user
        applicationEventPublisher.publishEvent(new PasswordResetEvent(authService.findUserByEmail(email), utility.applicationUrl(httpServletRequest)));
        return ResponseEntity.status(OK).body(new ResponseMessage("Password reset link sent", OK, null));
    }

    @PutMapping(path = "/new-password")
    public ResponseEntity<ResponseMessage> updateUserPassword(@RequestParam(name = "token") String tokenString,
                                                              @RequestBody @Valid PasswordResetModel passwordResetModel) {
        authService.passwordReset(tokenString, passwordResetModel);
        return ResponseEntity.ok(new ResponseMessage(PASSWORD_RESET_SUCCESS, OK, null));
    }


}
