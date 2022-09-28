package net.awazone.awazoneproject.model.userService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Auth {

    @Id
    @GeneratedValue(generator = "auth_id_generator", strategy = GenerationType.SEQUENCE)
    @SequenceGenerator(sequenceName = "auth_id_generator", allocationSize = 1, name = "auth_id_generator")
    private Long authId;

    @NotNull
    @Column(length = 500)
    private String token;
    @OneToOne
    private AwazoneUser awazoneUser;
    private String remoteAddress;
    private Boolean loggedIn;
    private String type;
    private LocalDateTime loggedInAt;
    private LocalDateTime loggedOutAt;
}
