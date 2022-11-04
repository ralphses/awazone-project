package net.awazone.awazoneproject.model.dtos.aibopay.coinremmitter;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class CreateInvoiceResponse {
    private String id;
    private String invoice_id;
    private String merchant_id;
    private String url;
    private Map<String, String> total_amount;
    private String[] paid_amount;
    private String usd_amount;
    private Map<String, String> conversion_rate;
    private String base_currency;
    private String coin;
    private String name;
    private String description;
    private String wallet_name;
    private String address;
    private String status;
    private String status_code;
    private String suceess_url;
    private String fail_url;
    private String notify_url;
    private boolean expire_on;
    private String invoice_date;
    private String custom_data1;
    private String custom_data2;
    private String last_updated_date;


}
