package net.awazone.awazoneproject.service.servicesImpl.user;

import net.awazone.awazoneproject.controller.exception.InvalidDomainException;
import net.awazone.awazoneproject.controller.exception.UsernameAlreadyExistsException;
import net.awazone.awazoneproject.model.requests.user.AwazoneUserAddressRequest;
import net.awazone.awazoneproject.model.requests.user.AwazoneUserContactRequest;
import net.awazone.awazoneproject.model.requests.user.NewRegistrationRequest;
import net.awazone.awazoneproject.model.userService.UserToken;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUserAddress;
import net.awazone.awazoneproject.model.userService.awazoneUserRole.AwazoneUserRole;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUserContact;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUserDomain;
import net.awazone.awazoneproject.repository.user.AwazoneUserRepository;
import net.awazone.awazoneproject.service.serviceInterfaces.user.AwazoneUserService;
import net.awazone.awazoneproject.service.serviceInterfaces.user.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static net.awazone.awazoneproject.controller.exception.ResponseMessage.*;

@Service
@Transactional
public class AwazoneUserServiceImp implements AwazoneUserService {

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AwazoneUserRepository awazoneUserRepository;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    public List<AwazoneUser> getAllUsers(int page) {
        Pageable pageable = PageRequest.of(page-1, 10);
        List<AwazoneUser> users = awazoneUserRepository.findAll(pageable).getContent();
        users.remove(0);
        return users;
    }

    @Override
    public AwazoneUser registerNewUser(NewRegistrationRequest newRegistrationRequest) {

        String userEmail = newRegistrationRequest.getEmail();

        Optional<AwazoneUser> optionalAwazoneUser = awazoneUserRepository.findByAwazoneUserContactEmail(userEmail);
        if(optionalAwazoneUser.isPresent()) {
            throw new UsernameAlreadyExistsException(USER_EXISTS);
        }
        String userDomain = userEmail
                .replace(userEmail.substring(userEmail.indexOf('@')), "")
                .replace(".", "_");

        AwazoneUser awazoneUser = AwazoneUser.builder()
                .fullName(newRegistrationRequest.getFullName())
                .password(passwordEncoder.encode(newRegistrationRequest.getPassword()))
                .awazoneUserContact(AwazoneUserContact.builder().email(userEmail).build())
                .awazoneUserDomain(AwazoneUserDomain.builder()
                        .domainName(userDomain)
                        .createdAt(LocalDateTime.now())
                        .isEnabled(true)
                        .build())
                .awazoneUserRole(AwazoneUserRole.builder()
                        .roleName("User")
                        .roleSet("USERxx")
                        .build())
                .isAccountNonLocked(false)
                .build();

        return awazoneUserRepository.save(awazoneUser);
    }

    @Override
    public void updateUserPassword(UserToken userToken, String newPassword) {
        userToken.getAwazoneUser().setPassword(passwordEncoder.encode(newPassword));
    }

    @Override
    public void updateUserPassword(AwazoneUser awazoneUser, String newPassword) {
        awazoneUser.setPassword(passwordEncoder.encode(newPassword));
    }

    @Override
    public int countAllById() {
        return awazoneUserRepository.countAllUsers();
    }

    @Override
    public int countAllByAccountNonLocked() {
        return awazoneUserRepository.countAllByUnlocked();
    }

    @Override
    public int countAllByEnabled() {
        return awazoneUserRepository.countAllEnabled();
    }

    @Override
    public AwazoneUser findAppUserById(Long userId) {
        return awazoneUserRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(INVALID_USER_ID));
    }

    @Override
    public AwazoneUser findAppUserByUsername(String username) {
        return awazoneUserRepository.findByAwazoneUserContactEmail(username).orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXIST));
    }

    @Override
    public AwazoneUser findAppUserByEmail(String email) {
        return findAppUserByUsername(email);
    }

    @Override
    public AwazoneUser findAppUserByDomain(String domain) {
        return awazoneUserRepository.findByUserDomainName(domain).orElseThrow(() -> new InvalidDomainException("Domain name not valid"));
    }

    @Override
    public void updateAppUserContactDetails(Long userId, AwazoneUserContactRequest awazoneUserContactRequest) {
        AwazoneUser awazoneUser = awazoneUserRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(INVALID_USER_ID));
        awazoneUser.setAwazoneUserContact(AwazoneUserContact.builder()
                        .mobilePhone(awazoneUserContactRequest.getMobilePhone())
                        .otherPhone(awazoneUserContactRequest.getOtherPhone())
                        .email(awazoneUserContactRequest.getEmail())
                .build());
    }

    @Override
    public void updateAppUserAddressDetails(Long userId, AwazoneUserAddressRequest awazoneUserAddressRequest) {
        AwazoneUser awazoneUser = awazoneUserRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(INVALID_USER_ID));
        awazoneUser.setAwazoneUserAddress(AwazoneUserAddress.builder()
                        .country(awazoneUserAddressRequest.getCountry())
                        .street(awazoneUserAddressRequest.getStreet())
                        .province(awazoneUserAddressRequest.getProvince())
                .build());
    }

    @Override
    public void updateAppUserDomainDetails(Long userId, String newDomainName) {
        AwazoneUser awazoneUser = awazoneUserRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException(INVALID_USER_ID));
        awazoneUser.getAwazoneUserDomain().setDomainName(newDomainName);
    }

    @Override
    public boolean isAccountActive(String username) {
        return findAppUserByEmail(username).isAccountNonLocked();
    }

    @Override
    public void assignRoleToUser(String roleName, Long userId) {
        AwazoneUser awazoneUser = findAppUserById(userId);
        AwazoneUserRole awazoneUserRole = userRoleService.getRoleByName(roleName);
        awazoneUser.setAwazoneUserRole(awazoneUserRole);
    }

    @Override
    public void removeRoleFromUser(String roleName, Long userId) {
        AwazoneUser awazoneUser = findAppUserById(userId);
        AwazoneUserRole awazoneUserRole = userRoleService.getRoleByName(roleName);
        awazoneUser.setAwazoneUserRole(awazoneUserRole);
    }
}
