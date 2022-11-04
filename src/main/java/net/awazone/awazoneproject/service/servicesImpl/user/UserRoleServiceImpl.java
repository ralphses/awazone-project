package net.awazone.awazoneproject.service.servicesImpl.user;

import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.exception.ResourceAlreadyExistException;
import net.awazone.awazoneproject.exception.ResourceNotFoundException;
import net.awazone.awazoneproject.model.user.awazoneUserRole.AwazoneUserAuthority;
import net.awazone.awazoneproject.model.user.awazoneUserRole.AwazoneUserRole;
import net.awazone.awazoneproject.model.dtos.user.NewRoleRequest;
import net.awazone.awazoneproject.repository.user.UserRoleRepository;
import net.awazone.awazoneproject.service.serviceInterfaces.user.UserRoleService;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Arrays.stream;
import static net.awazone.awazoneproject.exception.ResponseMessage.ROLE_NAME_TAKEN;
import static net.awazone.awazoneproject.exception.ResponseMessage.ROLE_NOT_FOUND;

@Service
@AllArgsConstructor
@Transactional
public class UserRoleServiceImpl implements UserRoleService {

    private final UserRoleRepository userRoleRepository;

    @Override
    public void createRole(NewRoleRequest newRoleRequest) {

        Optional<AwazoneUserRole> awazoneUserRoleOptional =
                userRoleRepository.findByRoleName(newRoleRequest.getRoleName());

        if(awazoneUserRoleOptional.isPresent()) {
            throw new ResourceAlreadyExistException(ROLE_NAME_TAKEN);
        }
        AwazoneUserRole awazoneUserRole = AwazoneUserRole.builder()
                .roleDescription(newRoleRequest.getRoleDescription())
                .roleSet("USERxx" + newRoleRequest.getRoleSet())
                .roleName(newRoleRequest.getRoleName())
                .build();
        userRoleRepository.save(awazoneUserRole);
    }

    @Override
    public void updateRoleDescription(String roleName, String newDescription) {
        AwazoneUserRole awazoneUserRole = userRoleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND));
        awazoneUserRole.setRoleDescription(newDescription);
    }

    @Override
    public void updateRoleName(String oldRoleName, String newRoleName){
        AwazoneUserRole awazoneUserRole = userRoleRepository.findByRoleName(oldRoleName)
                .orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND));
        awazoneUserRole.setRoleName(newRoleName);
    }

    @Override
    public void deleteRole(Long roleId){
        AwazoneUserRole awazoneUserRole = userRoleRepository.findById(roleId).orElseThrow(() -> new ResourceNotFoundException(ROLE_NOT_FOUND));
        userRoleRepository.delete(awazoneUserRole);
    }

    @Override
    public AwazoneUserRole getRoleByName(String roleName) {
        return userRoleRepository.findByRoleName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    @Override
    public List<String> getAllAuthorities() {
        return stream(AwazoneUserAuthority.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    @Override
    public List<AwazoneUserRole> allRoles(int page) {
        return userRoleRepository.findAll(PageRequest.of(page, 10)).getContent();
    }
}
