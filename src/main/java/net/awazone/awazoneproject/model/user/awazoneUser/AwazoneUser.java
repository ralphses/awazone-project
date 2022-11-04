package net.awazone.awazoneproject.model.user.awazoneUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.awazone.awazoneproject.model.user.awazoneUserRole.AwazoneUserRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.*;

import static java.util.Arrays.*;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.DETACH;
import static javax.persistence.FetchType.EAGER;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "awazone_user")
public class AwazoneUser implements UserDetails {

    @Id
    @SequenceGenerator(name = "user_id_generator", sequenceName = "user_id_generator", allocationSize = 1)
    @GeneratedValue(generator = "user_id_generator", strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Enumerated(EnumType.STRING)
    private AwazoneUserGender awazoneUserGender;

    @OneToOne(cascade = ALL, fetch = EAGER)
    private AwazoneUserContact awazoneUserContact;

    @OneToOne(cascade = ALL, fetch = EAGER)
    private AwazoneUserAddress awazoneUserAddress;

    @OneToOne(cascade = ALL, fetch = EAGER)
    private AwazoneUserDomain awazoneUserDomain;

    @OneToOne(cascade = ALL, fetch = EAGER)
    private AwazoneUserRole awazoneUserRole;
    private String password;
    private String referralCode;
    private LocalDate dateOfBirth;

    @OneToOne(cascade = DETACH, fetch = EAGER)
    private AwazoneUser referral;

    private boolean isAccountNonLocked;
    private boolean isCredentialsNonExpired;
    private boolean isAccountNonExpired;
    private boolean isEnabled;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        String roleSet = getAwazoneUserRole().getRoleSet();
        List<String> roleList = stream(roleSet.split("xx")).toList();

        roleList.forEach(role -> authorities.add(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase())));
        return authorities;
    }

    @Override
    public String getUsername() {
        return getAwazoneUserContact().getEmail();
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public boolean isAccountNonExpired() {
        return isAccountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return isAccountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return isCredentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

}
