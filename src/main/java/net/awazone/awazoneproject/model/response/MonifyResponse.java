package net.awazone.awazoneproject.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MonifyResponse {

    private boolean requestSuccessful;
    private String responseMessage;
    private Integer responseCode;
    private Object responseBody;
}
