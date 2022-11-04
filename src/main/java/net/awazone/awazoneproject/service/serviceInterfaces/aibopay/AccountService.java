package net.awazone.awazoneproject.service.serviceInterfaces.aibopay;

import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.model.aibopay.account.AibopayAccount;
import net.awazone.awazoneproject.model.dtos.aibopay.ActivateAccountWithBvnRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.NewAccountRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.NewDeactivateAccountRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.UpdateBvnRequest;
import org.springframework.http.ResponseEntity;


public interface AccountService {
    void newAccount(Long userId, NewAccountRequest newAccountRequest);
    String accountStatus(String accountNumber);
    AibopayAccount findAccountByReference(String accountReference);

    ResponseEntity<ResponseMessage> findAllAccounts(Integer page);
    AibopayAccount getAccount(String accountNumber);

    ResponseMessage findAccountByAccountNumber(String accountNumber);

    ResponseEntity<ResponseMessage> updateBvn(UpdateBvnRequest updateBvnRequest);

    ResponseMessage fetchVirtualAccount(String accountReference);

    ResponseMessage deactivateAccount(NewDeactivateAccountRequest newDeactivateAccountRequest);

    ResponseMessage activateWithBvn(ActivateAccountWithBvnRequest activateAccountWithBvnRequest);
}
