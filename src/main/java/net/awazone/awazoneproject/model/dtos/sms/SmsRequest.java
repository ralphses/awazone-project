package net.awazone.awazoneproject.model.dtos.sms;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsRequest {
    private String api_key;
    private String type;
    private String channel;
    private String to;
    private String sms;
    private String from;
}
