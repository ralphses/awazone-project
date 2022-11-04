package net.awazone.awazoneproject.model.dtos.aibopay.monnify;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewDeactivateAccountRequest {
    private String username;
    private String password;
    private String accountReference;
}
