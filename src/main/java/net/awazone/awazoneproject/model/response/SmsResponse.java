package net.awazone.awazoneproject.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SmsResponse {

    private String code;
    private String message_id;
    private String message_id_str;
    private String message;
    private String user;
    private double balance;
}
