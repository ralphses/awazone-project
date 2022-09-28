package net.awazone.awazoneproject.service.serviceInterfaces.aibopay;

import net.awazone.awazoneproject.model.requests.aibopay.NewAccountRequest;

public interface AccountService {
    void newAccount(Long userId, NewAccountRequest newAccountRequest);
    String accountStatus(Long accountNumber);
}
