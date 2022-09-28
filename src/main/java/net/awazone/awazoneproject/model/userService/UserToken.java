package net.awazone.awazoneproject.model.userService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.EAGER;
import static javax.persistence.GenerationType.SEQUENCE;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserToken {

    @Id
    @SequenceGenerator(name = "user_token_id", sequenceName = "user_token_id", allocationSize = 1)
    @GeneratedValue(generator = "user_token_id", strategy = SEQUENCE)
    private Long id;

    @Enumerated(STRING)
    private TokenType tokenType;

    @OneToOne(fetch = EAGER, cascade = DETACH)
    private AwazoneUser awazoneUser;

    private String tokenString;
    private boolean used = false;
    private LocalDateTime activatedAt;
    private LocalDateTime expiresAt;
    private LocalDateTime createdAt = LocalDateTime.now();
}
