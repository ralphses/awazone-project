package net.awazone.awazoneproject.model.mobileUtility;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class DataPackage {
    private String code;
    private String airtime_cost;
    private String cost;
    private String value;
    private String network;
    private String _package;
    private String validity;
    private String status;
}
