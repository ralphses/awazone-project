package net.awazone.awazoneproject.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CardPaymentResponse {

    private String status;
    private String message;
    private String transactionReference;
    private String paymentReference;
    private double authorizedAmount;
    private OtpData otpData;

}


