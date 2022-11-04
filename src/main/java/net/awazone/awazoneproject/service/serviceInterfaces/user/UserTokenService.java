package net.awazone.awazoneproject.service.serviceInterfaces.user;

import net.awazone.awazoneproject.model.user.TokenType;
import net.awazone.awazoneproject.model.user.UserToken;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUser;

public interface UserTokenService {
    UserToken verifyToken(String token, TokenType tokenType);

    void createNewToken(AwazoneUser appUser, String token, TokenType tokenType);
}
