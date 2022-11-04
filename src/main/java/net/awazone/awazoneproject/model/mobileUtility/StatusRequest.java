package net.awazone.awazoneproject.model.mobileUtility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
public class StatusRequest {

    @NotBlank
    protected String service_id;
    @NotBlank
    protected String requestType;

}
