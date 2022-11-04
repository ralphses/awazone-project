package net.awazone.awazoneproject.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestInformation {
    private String service;
    private String name;
    private String discount;
    private String status;
    private boolean blocked;
}
