package net.awazone.awazoneproject.model.dtos.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@AllArgsConstructor
public class NewRoleRequest {
    @NotBlank
    @NotNull
    private String roleName;
    @NotBlank
    @NotNull
    private String roleSet;

    private String roleDescription = "";
}
