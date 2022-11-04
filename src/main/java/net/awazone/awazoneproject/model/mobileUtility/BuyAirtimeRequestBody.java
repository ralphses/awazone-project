package net.awazone.awazoneproject.model.mobileUtility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyAirtimeRequestBody {
    private String service_id;
    private String phoneNumber;
    private String trans_id;
    private String service_type;
    private double amount;
}
