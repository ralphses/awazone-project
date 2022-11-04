package net.awazone.awazoneproject.model.dtos.aibopay.monnify;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewMonifyPaymentInitRequest {

    private Double amount;
    private String customerName;
    private String customerEmail;
    private String paymentReference;
    private String paymentDescription;
    private String currencyCode;
    private String contractCode;
    private String redirectUrl;
    private String[] paymentMethods;

}
