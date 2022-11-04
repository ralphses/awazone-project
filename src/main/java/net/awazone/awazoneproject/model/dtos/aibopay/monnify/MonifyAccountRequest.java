package net.awazone.awazoneproject.model.dtos.aibopay.monnify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonifyAccountRequest {

    private String accountReference;
    private String accountName;
    private String currencyCode;
    private String contractCode;
    private String customerEmail;
    private String customerName;
    private String bvn;
    private boolean getAllAvailableBanks;
    private String[] preferredBanks;

}
