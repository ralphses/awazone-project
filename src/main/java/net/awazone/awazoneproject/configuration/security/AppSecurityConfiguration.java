package net.awazone.awazoneproject.configuration.security;

import net.awazone.awazoneproject.configuration.security.jwt.JwtTokenVerifier;
import net.awazone.awazoneproject.configuration.security.jwt.JwtUsernameAndPasswordAuthenticationFilter;
import net.awazone.awazoneproject.model.aibopay.UserWallet;
import net.awazone.awazoneproject.model.aibopay.account.AibopayAccount;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUserAddress;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUserContact;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUserDomain;
import net.awazone.awazoneproject.model.user.awazoneUserRole.AwazoneUserRole;
import net.awazone.awazoneproject.repository.aibopay.AccountRepository;
import net.awazone.awazoneproject.repository.aibopay.UserWalletRepository;
import net.awazone.awazoneproject.repository.user.AwazoneUserRepository;
import net.awazone.awazoneproject.service.servicesImpl.user.JwtConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static net.awazone.awazoneproject.model.aibopay.account.AccountStatus.ENABLED;
import static net.awazone.awazoneproject.model.aibopay.account.AccountType.SAVINGS;
import static net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUserGender.MALE;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@EnableWebSecurity
@EnableGlobalMethodSecurity(
        prePostEnabled = true,
        securedEnabled = true,
        jsr250Enabled = true)
public class AppSecurityConfiguration {

    private final SecretKey secretKey;

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
                    "/api/v1/admin/super/activate",
                    "/api/v1/aibopay/utility/data-bundles",
                    "/api/v1/aibopay/utility/buy-data",
                    "/api/v1/aibopay/utility/buy-airtime",
                    "/api/v1/aibopay/manual/new-manual-payment",
                    "/api/v1/aibopay/manual/get/one/download",
                    "/api/v1/aibopay/payment/card",
                    "/api/v1/aibopay/payment/status",
                    "/api/v1/aibopay/payment/initialize",
                    "/api/v1/aibopay/payment/deposit",
                    "/api/v1/aibopay/payment/transfer",
                    "/api/v1/aibopay/payment/get/all-banks",
                    "/api/v1/aibopay/payment/crypto/{coinType}",
                    "/api/v1/aibopay/payment/get/payment",
                    "/api/v1/aibopay/payment/crypto",
                    "/api/v1/aibopay/payment/transaction/notification"
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
    CommandLineRunner commandLineRunner(AwazoneUserRepository awazoneUserRepository, UserWalletRepository userWalletRepository, AccountRepository accountRepository) {
        return args -> {

            AwazoneUser awazoneUserAdmin = AwazoneUser.builder()
                    .referralCode("super_admin")
                    .awazoneUserContact(AwazoneUserContact.builder()
                            .email("super_admin1@awazone.net")
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

            awazoneUserRepository.save(awazoneUserAdmin);

            AibopayAccount awazoneAccount = AibopayAccount.builder()
                    .accountStatus(ENABLED)
                    .bvn("22222222222")
                    .accountType(SAVINGS)
                    .currentBalance(BigDecimal.valueOf(0.0))
                    .createdAt(LocalDateTime.now())
                    .activatedAt(LocalDateTime.now())
                    .customerName(awazoneUserAdmin.getFullName())
                    .accountReference("Awazone")
                    .accountNumber("5000516888")
                    .build();

            accountRepository.save(awazoneAccount);

            UserWallet wallet = UserWallet.builder()
                    .address("AWAZONE")
                    .awazoneUser(awazoneUserAdmin)
                    .account(awazoneAccount)
                    .build();
            userWalletRepository.save(wallet);

        };
    }
}
