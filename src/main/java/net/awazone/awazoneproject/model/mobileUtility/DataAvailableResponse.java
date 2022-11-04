package net.awazone.awazoneproject.model.mobileUtility;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataAvailableResponse {
    private int available;
    private Object auto;
    private String strength;
    private Object mega;

}
