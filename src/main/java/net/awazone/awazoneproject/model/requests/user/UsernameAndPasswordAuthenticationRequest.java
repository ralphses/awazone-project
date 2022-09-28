package net.awazone.awazoneproject.model.requests.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class UsernameAndPasswordAuthenticationRequest {
    private String username;
    private String password;
}
