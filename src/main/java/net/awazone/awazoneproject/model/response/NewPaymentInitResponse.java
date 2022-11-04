package net.awazone.awazoneproject.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NewPaymentInitResponse {
    private String transactionReference;
    private String paymentReference;
    private String merchantName;
    private String apiKey;
    private String[] enabledPaymentMethod;
    private String redirectUrl;
    private String checkoutUrl;

}
