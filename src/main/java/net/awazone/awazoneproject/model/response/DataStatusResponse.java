package net.awazone.awazoneproject.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DataStatusResponse {

    private String message;
    private int statusCode;
    private RequestInformation data;
}
