package net.awazone.awazoneproject.service.servicesImpl.user;

import lombok.RequiredArgsConstructor;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.repository.user.AwazoneUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserService implements UserDetailsService {

    private final AwazoneUserRepository awazoneUserRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AwazoneUser awazoneUser = awazoneUserRepository.findByAwazoneUserContactEmail(email).orElseThrow(() -> new UsernameNotFoundException("Invalid credentials"));
        return new User(awazoneUser.getAwazoneUserContact().getEmail(), awazoneUser.getPassword(), awazoneUser.getAuthorities());
    }

//    private Collection<? extends GrantedAuthority> getAuthorities(Set<AwazoneUserRole> awazoneUserAuthorities) {
//        List<SimpleGrantedAuthority> simpleGrantedAuthorities = new ArrayList<>();
//        awazoneUserAuthorities.forEach(awazoneUserRole -> simpleGrantedAuthorities.add(new SimpleGrantedAuthority(awazoneUserRole.getAuthorityName())));
//        return simpleGrantedAuthorities;
//    }
}
