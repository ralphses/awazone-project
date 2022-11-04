package net.awazone.awazoneproject.model.mobileUtility;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class MobileUtilityResponse {

    private String message;
    private String statusCode;
    Object details;
}
