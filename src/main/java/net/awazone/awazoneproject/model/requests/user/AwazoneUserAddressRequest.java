package net.awazone.awazoneproject.model.requests.user;

import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
public class AwazoneUserAddressRequest {

    @NotEmpty
    @NotBlank
    @NotNull
    private String street;

    @NotEmpty
    @NotBlank
    @NotNull
    private String homeAddress;

    @NotEmpty
    @NotBlank
    @NotNull
    private String country;

    @NotEmpty
    @NotBlank
    @NotNull
    private String province;
}
