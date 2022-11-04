package net.awazone.awazoneproject.model.dtos.aibopay.coinremmitter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewCryptoPaymentRequest {
    private String api_key;
    private String password;
    private double amount;
    private String notify_url;
    private String suceess_url;
    private String fail_url;
    private String description;

}
