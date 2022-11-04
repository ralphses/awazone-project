package net.awazone.awazoneproject.model.aibopay.transaction.virtualTransactions;

import lombok.Data;

@Data
public class CustomerDTO {
    private String email;
    private String name;
    private String merchantCode;
}
