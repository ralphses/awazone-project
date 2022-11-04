package net.awazone.awazoneproject.service.servicesImpl.user;

import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.exception.CustomInvalidParamException;
import net.awazone.awazoneproject.exception.IllegalUserException;
import net.awazone.awazoneproject.exception.ResourceNotFoundException;
import net.awazone.awazoneproject.model.dtos.user.AwazoneUserAddressRequest;
import net.awazone.awazoneproject.model.dtos.user.AwazoneUserContactRequest;
import net.awazone.awazoneproject.model.dtos.user.NewRegistrationRequest;
import net.awazone.awazoneproject.model.user.UserToken;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUserAddress;
import net.awazone.awazoneproject.model.user.awazoneUserRole.AwazoneUserRole;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUserContact;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUserDomain;
import net.awazone.awazoneproject.repository.user.AwazoneUserRepository;
import net.awazone.awazoneproject.service.serviceInterfaces.user.AwazoneUserService;
import net.awazone.awazoneproject.service.serviceInterfaces.user.UserRoleService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static net.awazone.awazoneproject.exception.ResponseMessage.*;

@Service
@Transactional
@AllArgsConstructor
public class AwazoneUserServiceImp implements AwazoneUserService {
    private final PasswordEncoder passwordEncoder;

    private final AwazoneUserRepository awazoneUserRepository;
    private final UserRoleService userRoleService;

    @Override
    public int countAllById() {
        return awazoneUserRepository.countAllUsers();
    }

    @Override
    public List<AwazoneUser> getAllUsers(int page) {
        Pageable pageable = PageRequest.of(page-1, 10);
        List<AwazoneUser> users = awazoneUserRepository.findAll(pageable).getContent();
        users.remove(0);
        return users;
    }

    @Override
    public AwazoneUser registerNewUser(NewRegistrationRequest newRegistrationRequest) {

        AwazoneUser referrer = (!newRegistrationRequest.getReferrer().equalsIgnoreCase(""))
                ? findByReferralCode(newRegistrationRequest.getReferrer()) : null;

        String userEmail = newRegistrationRequest.getEmail();

        //Check for duplicate email address
        Optional<AwazoneUser> optionalAwazoneUser =
                awazoneUserRepository.findByAwazoneUserContactEmail(userEmail);

        if(optionalAwazoneUser.isPresent()) {
            throw new CustomInvalidParamException(USER_EXISTS);
        }

        //Build domain name for this user
        String userDomain = userEmail
                .replace(userEmail.substring(userEmail.indexOf('@')), "")
                .replace(".", "_");

       validateDomainName(userDomain);

        String referralCode = generateReferralCode(userDomain);

        //Build user object
        AwazoneUser awazoneUser = AwazoneUser.builder()
                .fullName(newRegistrationRequest.getFullName())
                .password(passwordEncoder.encode(newRegistrationRequest.getPassword()))
                .referralCode(referralCode)
                .referral(referrer)
                .awazoneUserContact(AwazoneUserContact.builder().email(userEmail).build())
                .awazoneUserAddress(AwazoneUserAddress.builder().build())
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

    private void validateDomainName(String userDomain) {
        if(userDomain.equalsIgnoreCase("super_admin")) {
            throw new IllegalUserException("Reserved domain name " + userDomain);
        }
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
    public void updateAppUserAddressDetails(Long userId, AwazoneUserAddressRequest awazoneUserAddressRequest, Principal principal) {

        AwazoneUser awazoneUser = findAppUserById(userId);

        authenticateUser(principal, awazoneUser);

        awazoneUser.getAwazoneUserAddress().setHouseAddress(awazoneUserAddressRequest.getHomeAddress());
        awazoneUser.getAwazoneUserAddress().setCountry(awazoneUserAddressRequest.getCountry());
        awazoneUser.getAwazoneUserAddress().setProvince(awazoneUserAddressRequest.getProvince());
        awazoneUser.getAwazoneUserAddress().setStreet(awazoneUserAddressRequest.getStreet());

    }

    @Override
    public AwazoneUser findByReferralCode(String referralCode) {
        return awazoneUserRepository.findByReferralCode(referralCode)
                .orElseThrow(() -> new ResourceNotFoundException("Invalid referral code " + referralCode));
    }

    @Override
    public Map<String, List<AwazoneUser>> fetchUserReferralTree(String username, Authentication authentication) {

        Map<String, List<AwazoneUser>> userTree = new HashMap<>();

        boolean authorizedUser = authentication.getAuthorities().contains("ROLE_SUPER:ADMIN") ||
                authentication.getName().equals(username);

        if(!authorizedUser) {
            throw new IllegalUserException("Not allowed");
        }

        AwazoneUser user = findAppUserByEmail(username);

        //Get first level referrals
        List<AwazoneUser> firstLevel = awazoneUserRepository.findByReferral(user);

        //Get second level referrals
        List<AwazoneUser> secondLevel = new ArrayList<>();

        firstLevel.forEach(thisUser -> secondLevel.addAll(awazoneUserRepository.findByReferral(thisUser)));

        //Get third level referrals
        List<AwazoneUser> thirdLevel = new ArrayList<>();

        secondLevel.forEach(thisUser -> thirdLevel.addAll(awazoneUserRepository.findByReferral(thisUser)));

        userTree.put("first level", firstLevel);
        userTree.put("second level", secondLevel);
        userTree.put("third level", thirdLevel);

        Stream<Map.Entry<String, List<AwazoneUser>>> sortedTree = userTree.entrySet().stream().sorted(Map.Entry.comparingByKey());

        return userTree;
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
        return awazoneUserRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException(INVALID_USER_ID));
    }

    @Override
    public AwazoneUser findAppUserByUsername(String username) {
        return awazoneUserRepository.findByAwazoneUserContactEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(USER_DOES_NOT_EXIST));
    }

    @Override
    public AwazoneUser findAppUserByEmail(String email) {
        return findAppUserByUsername(email);
    }

    @Override
    public AwazoneUser findAppUserByDomain(String domain) {
        return awazoneUserRepository.findByUserDomainName(domain)
                .orElseThrow(() -> new CustomInvalidParamException("Domain name not valid"));
    }

    @Override
    public void updateAppUserContactDetails(Long userId,
                                            AwazoneUserContactRequest awazoneUserContactRequest,
                                            Principal principal) {

        AwazoneUser awazoneUser = findAppUserById(userId);

        authenticateUser(principal, awazoneUser);

        awazoneUser.getAwazoneUserContact().setMobilePhone(awazoneUserContactRequest.getMobilePhone());
        awazoneUser.getAwazoneUserContact().setOtherPhone(awazoneUserContactRequest.getOtherPhone());
    }

    @Override
    public void updateAppUserDomainDetails(Long userId, String newDomainName, Principal principal) {

        validateDomainName(newDomainName);

        AwazoneUser awazoneUser = findAppUserById(userId);

        authenticateUser(principal, awazoneUser);
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

    private void authenticateUser(Principal principal, AwazoneUser awazoneUser) {
        if(!Objects.equals(principal.getName(), awazoneUser.getAwazoneUserContact().getEmail())) {
            throw new IllegalUserException("Unauthorized user");
        }
    }

    private String generateReferralCode(String userDomain) {
        return userDomain + UUID.randomUUID().toString().replace("-", "").substring(0, 5);
    }
}
