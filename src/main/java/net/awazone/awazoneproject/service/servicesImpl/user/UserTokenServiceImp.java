package net.awazone.awazoneproject.service.servicesImpl.user;

import net.awazone.awazoneproject.controller.exception.InvalidTokenException;
import net.awazone.awazoneproject.model.userService.TokenType;
import net.awazone.awazoneproject.model.userService.UserToken;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.repository.user.UserTokenRepository;
import net.awazone.awazoneproject.service.serviceInterfaces.user.UserTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static net.awazone.awazoneproject.controller.exception.MainException.INVALID_TOKEN_MESSAGE;
import static net.awazone.awazoneproject.controller.exception.ResponseMessage.*;

@Service
@Transactional
public class UserTokenServiceImp implements UserTokenService {

    @Autowired
    private UserTokenRepository userTokenRepository;

    @Override
    public UserToken verifyToken(String token, TokenType tokenType) {
        UserToken userToken = userTokenRepository.findByTokenStringAAndTokenType(token, tokenType).orElseThrow(() -> new InvalidTokenException(INVALID_TOKEN_MESSAGE));

        if(LocalDateTime.now().isAfter(userToken.getExpiresAt())) {
            userTokenRepository.delete(userToken);
            throw new InvalidTokenException(TOKEN_EXPIRED);
        }
        //Activate this user account
        AwazoneUser awazoneUser = userToken.getAwazoneUser();
        awazoneUser.setAccountNonLocked(true);
        awazoneUser.setAccountNonExpired(true);
        awazoneUser.setCredentialsNonExpired(true);


        userTokenRepository.delete(userToken);
        return userToken;
    }

    @Override
    public void createNewToken(AwazoneUser awazoneUser, String tokenString, TokenType tokenType) {
        createNewTokenHere(awazoneUser, tokenString, tokenType);
    }

    private void createNewTokenHere(AwazoneUser awazoneUser, String tokenString, TokenType tokenType) {
        userTokenRepository.save(
                UserToken.builder()
                        .createdAt(LocalDateTime.now())
                        .tokenString(tokenString)
                        .awazoneUser(awazoneUser)
                        .expiresAt(LocalDateTime.now().plusMinutes(10))
                        .tokenType(tokenType)
                        .build());
    }
}
