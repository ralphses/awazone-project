package net.awazone.awazoneproject.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUser;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class JwtToken {

    @Id
    @GeneratedValue(generator = "jwt_token_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "jwt_token_id_generator", allocationSize = 1, name = "jwt_token_id_generator")
    private Long jwtTokenId;

    @Column(length = 5000)
    private String jwtTokenString;
    @OneToOne
    private AwazoneUser awazoneUser;
    private String remoteAddress;
    private Boolean loggedIn;
    private Boolean valid;
    private String type;
    private LocalDateTime loggedInAt;
    private LocalDateTime loggedOutAt;
}
