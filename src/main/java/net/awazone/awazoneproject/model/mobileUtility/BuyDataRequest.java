package net.awazone.awazoneproject.model.mobileUtility;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.Pattern;

@Data
@Builder
public class BuyDataRequest {

    private String type; //BCA_SME
    private String dataValue; //500
    private String phoneNumber;
    private String code; //MS500

//    @Pattern(regexp = "\\d")
    private String cost; //140
    private String email;

}
