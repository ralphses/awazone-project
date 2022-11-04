package net.awazone.awazoneproject.model.mobileUtility;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BuyDataRequestBody{

    private String service_id;
    private String service_type;
    private String beneficiary;
    private String code;
    private String trans_id;
    private int amount;


}
