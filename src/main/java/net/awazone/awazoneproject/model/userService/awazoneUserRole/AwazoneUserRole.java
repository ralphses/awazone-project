package net.awazone.awazoneproject.model.userService.awazoneUserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

import static javax.persistence.GenerationType.*;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_role")
public class AwazoneUserRole {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long roleId;

    @Column(length = 1500)
    private String roleSet;

    @Column(length = 30)
    private String roleName;

    @Column(length = 150)
    private String roleDescription;
}
