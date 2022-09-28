package net.awazone.awazoneproject.configuration.security;

import net.awazone.awazoneproject.configuration.security.jwt.JwtTokenVerifier;
import net.awazone.awazoneproject.configuration.security.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUserAddress;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUserContact;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUserDomain;
import net.awazone.awazoneproject.model.userService.awazoneUserRole.AwazoneUserRole;
import net.awazone.awazoneproject.repository.user.AwazoneUserRepository;
import net.awazone.awazoneproject.service.serviceInterfaces.user.UserRoleService;
import net.awazone.awazoneproject.service.servicesImpl.user.JwtConfig;
import net.awazone.awazoneproject.utility.Utility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import static net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUserGender.MALE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class AppSecurityConfiguration {

    private final SecretKey secretKey;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private UserRoleService userRoleService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    @Autowired
    private Utility utility;
    private final JwtConfig jwtConfig;

    private static final String[] WHITE_LIST_URL =
            {
                    "/user/refresh",
                    "/user/register",
                    "/user/login",
                    "/user/logout",
                    "/user/activate",
                    "/user/reactivate-link",
                    "/user/password-reset",
                    "/user/new-password",
                    "/api/v1/admin/super/activate"
//                    "/api/**"
            };

    public AppSecurityConfiguration(SecretKey secretKey, JwtConfig jwtConfig) {
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, AuthenticationManager authenticationManager) throws Exception {

        httpSecurity
                .cors().and().csrf().disable()
                .sessionManagement().sessionCreationPolicy(STATELESS).and()

                .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager, secretKey, jwtConfig))
                .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class)

                .authorizeRequests()
                .antMatchers(WHITE_LIST_URL).permitAll()
                .antMatchers("/api/**").authenticated()
                .anyRequest().authenticated()
                .and()
                .logout().logoutUrl("/user/logout")
                .logoutSuccessHandler(new CustomSuccessLogoutHandler())
                .deleteCookies();

     return  httpSecurity.build();
    }

    @Bean
    public AuthenticationManager getAuthenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(11);
    }

    @Bean
    CommandLineRunner commandLineRunner(AwazoneUserRepository awazoneUserRepository) {
        return args -> {

            AwazoneUser awazoneUserAdmin = AwazoneUser.builder()
                    .awazoneUserContact(AwazoneUserContact.builder()
                            .email("super_admin@awazone.net")
                            .build())
                    .awazoneUserDomain(AwazoneUserDomain.builder()
                            .domainName("super_admin")
                            .createdAt(LocalDateTime.now())
                            .isEnabled(true).build())
                    .awazoneUserRole(AwazoneUserRole.builder()
                            .roleName("SUPER_ADMIN")
                            .roleSet("SUPER:ADMINxxADMIN_VIEW_USERSxx")
                            .roleDescription("Has all authorities for this System")
                            .build())
                    .fullName("Edime Joshua")
                    .isAccountNonLocked(false)
                    .password(passwordEncoder().encode("password"))
                    .awazoneUserAddress(AwazoneUserAddress.builder()
                            .country("NIGERIA")
                            .province("Nasarawa")
                            .street("Makurdi Road")
                            .houseAddress("Deapples Global")
                            .build())
                    .awazoneUserGender(MALE)
                    .isAccountNonExpired(true)
                    .isCredentialsNonExpired(true)
                    .isEnabled(false)
                    .build();
        };
    }
}
