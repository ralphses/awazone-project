package net.awazone.awazoneproject.model.mobileUtility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BuyAirtimeRequest {
    private String service_id;
    private String phoneNumber;
    private double amount;
    private String email;
}
