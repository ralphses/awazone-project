package net.awazone.awazoneproject.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PaymentInitResponse {

    private String accountNumber;
    private String accountName;
    private String bankName;
    private String bankCode;
    private String accountDurationSeconds;
    private String ussdPayment;
    private String requestTime;
    private String transactionReference;
    private String paymentReference;
    private String amount;
    private String fee;
    private String totalPayable;
    private String expiresOn;
    private String collectionChannel;
    private Object productInformation;
}
