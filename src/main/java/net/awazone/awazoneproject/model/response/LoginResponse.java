package net.awazone.awazoneproject.model.response;

import lombok.Data;

@Data
public class LoginResponse {
    private String accessToken;
    private Long expiresIn;
}
