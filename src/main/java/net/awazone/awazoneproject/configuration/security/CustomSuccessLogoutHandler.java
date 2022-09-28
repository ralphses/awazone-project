package net.awazone.awazoneproject.configuration.security;

import net.awazone.awazoneproject.controller.exception.IllegalUserException;
import net.awazone.awazoneproject.model.userService.JwtToken;
import net.awazone.awazoneproject.service.servicesImpl.user.AuthServiceImpl;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static net.awazone.awazoneproject.controller.exception.ResponseMessage.ILLEGAL_USER;

@Service
@Transactional
public class CustomSuccessLogoutHandler implements LogoutSuccessHandler {

    @Override
    @Transactional
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

        List<JwtToken> jwtTokens = AuthServiceImpl.authService.findByAddress(request.getRemoteAddr());

        Boolean isLoggedIn = jwtTokens.stream().map(JwtToken::getLoggedIn).reduce(true, (first, second) -> first = second);

        if(jwtTokens.isEmpty() || jwtTokens.size() < 2  || !isLoggedIn) {
            throw new IllegalUserException(ILLEGAL_USER);
        }

        jwtTokens.forEach(token -> {
            token.setLoggedOutAt(LocalDateTime.now());
            token.setValid(false);
            token.setLoggedIn(false);
        });

        AuthServiceImpl.authService.saveAllJwts(jwtTokens);
    }
}
