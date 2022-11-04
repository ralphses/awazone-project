package net.awazone.awazoneproject.model.response;

import lombok.Data;

@Data
public class Account {
    private String bankCode;
    private String bankName;
    private String accountNumber;
    private String accountName = "";
}
