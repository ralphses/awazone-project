package net.awazone.awazoneproject.model.dtos.aibopay.monnify;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class MonnifyValidateBvnRequest {

    private String bvn;
    private String name;
    private String dateOfBirth;
    private String mobileNo;
}
