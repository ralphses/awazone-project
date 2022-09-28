package net.awazone.awazoneproject.service.serviceInterfaces.user;

import net.awazone.awazoneproject.model.userService.TokenType;
import net.awazone.awazoneproject.model.userService.UserToken;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;

public interface UserTokenService {
    UserToken verifyToken(String token, TokenType tokenType);

    void createNewToken(AwazoneUser appUser, String token, TokenType tokenType);
}
