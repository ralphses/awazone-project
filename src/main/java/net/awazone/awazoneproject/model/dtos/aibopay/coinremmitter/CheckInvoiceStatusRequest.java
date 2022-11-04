package net.awazone.awazoneproject.model.dtos.aibopay.coinremmitter;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CheckInvoiceStatusRequest {

    private String api_key;
    private String password;
    private String invoice_id;
}
