package net.awazone.awazoneproject.model.userService.awazoneUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
@Entity
@Table(name = "user_domain_table")
public class AwazoneUserDomain {

    @Id
    @SequenceGenerator(
            name = "user_domain_id_generator",
            sequenceName = "user_domain_id_generator",
            allocationSize = 1
    )
    @GeneratedValue(
            generator = "user_domain_id_generator",
            strategy = GenerationType.SEQUENCE
    )
    @Column(name = "user_domain_identity")
    private Long id;

    @Column(
            nullable = false,
            unique = true
    )
    private String domainName;

    private boolean isEnabled = true;
    private LocalDateTime createdAt;
}
