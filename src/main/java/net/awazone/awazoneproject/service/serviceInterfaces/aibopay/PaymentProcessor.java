package net.awazone.awazoneproject.service.serviceInterfaces.aibopay;

import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.model.dtos.aibopay.*;
import net.awazone.awazoneproject.model.dtos.aibopay.coinremmitter.NewCryptoPaymentRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.NewCardPaymentRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.NewInitPaymentRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.NewTransferRequest;
import net.awazone.awazoneproject.model.dtos.sms.SmsRequest;
import net.awazone.awazoneproject.model.response.ActivateBvnResponse;
import net.awazone.awazoneproject.model.response.Bank;
import net.awazone.awazoneproject.model.response.VirtualAccountResponse;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.service.servicesImpl.aibopay.CredentialType;

import java.util.List;


public interface PaymentProcessor {

    VirtualAccountResponse createVirtualAccount(AwazoneUser awazoneUser, String bvn, String fullName);

    Object updateCredentials(CredentialType credentialType, String accountReference, Object credential);

    Object fetchVirtualAccount(String accountReference);

    void deactivateVirtualAccount(String accountReference);

    void initiateSingleTransfer(NewTransferRequest newTransferRequest, String reference);

    Object getTransactionsForAccount(String accountReference, int page, int numberOfRecord);
    Object getTransactionsForAccount(String accountReference);

    ResponseMessage acceptCardPayment(NewCardPaymentRequest newCardPaymentRequest);

    ResponseMessage acceptCryptoPayment(NewCryptoPaymentRequest newCryptoPaymentRequest);

    ResponseMessage paymentStatus(String transactionReference);

    ResponseMessage  getPayment(String paymentReference);

    ResponseMessage initPayment(NewInitPaymentRequest newInitPaymentRequest);

    ActivateBvnResponse validateBvn(ActivateAccountWithBvnRequest activateAccountWithBvnRequest);

    ResponseMessage acceptTransferPayment(NewPayWithBankTransferRequest newPayWithBankTransferRequest);

    List<Bank> getAllBanksAndUSSDCodes();

    void sendSms(SmsRequest smsRequest);
}
