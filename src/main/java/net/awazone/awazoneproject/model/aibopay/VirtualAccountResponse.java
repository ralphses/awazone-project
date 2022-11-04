package net.awazone.awazoneproject.model.aibopay;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
public class VirtualAccountResponse {
    private String contractCode;
    private String accountReference;
    private String accountName;
    private String currencyCode;
    private String customerEmail;
    private String customerName;
    private List<Account> accounts;
    private String collectionChannel;
    private String reservationReference;
    private String reservedAccountType;
    private String status;
    private LocalDateTime createdOn;
    private String[] incomeSplitConfig;
    private String bvn;
    private boolean restrictPaymentSource;

    @Builder
    private static class Account {
        private String bankCode;
        private String bankName;
        private String accountNumber;
        private String accountName;
    }
}
