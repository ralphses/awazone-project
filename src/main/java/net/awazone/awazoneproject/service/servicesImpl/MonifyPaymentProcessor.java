package net.awazone.awazoneproject.service.servicesImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.configuration.monnify.MonifyCredentials;
import net.awazone.awazoneproject.configuration.termii.TermiiConfig;
import net.awazone.awazoneproject.exception.EmailNotSentException;
import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.exception.UnsuccessfulRequestException;
import net.awazone.awazoneproject.exception.VirtualAccountException;
import net.awazone.awazoneproject.model.dtos.aibopay.*;
import net.awazone.awazoneproject.model.dtos.aibopay.coinremmitter.NewCryptoPaymentRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.*;
import net.awazone.awazoneproject.model.dtos.sms.SmsRequest;
import net.awazone.awazoneproject.model.response.*;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.service.serviceInterfaces.aibopay.PaymentProcessor;
import net.awazone.awazoneproject.service.servicesImpl.aibopay.CredentialType;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;

import static net.awazone.awazoneproject.configuration.MainConfig.PAYMENT_REDIRECT_URL;
import static net.awazone.awazoneproject.configuration.monnify.MonifyCnfig.*;
import static net.awazone.awazoneproject.service.servicesImpl.aibopay.CredentialType.BVN;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@Service
@AllArgsConstructor
public class MonifyPaymentProcessor implements PaymentProcessor {

    private final MonifyCredentials monifyCredentials;

    @Override
    public VirtualAccountResponse createVirtualAccount(AwazoneUser awazoneUser, String bvn, String fullName) {
        String[] preferredBanks = {WEMA_BANK_CODE};

        //Build new Account request
        MonifyAccountRequest monifyAccountRequest = MonifyAccountRequest.builder()
                .accountName(fullName)
                .customerEmail(awazoneUser.getAwazoneUserContact().getEmail())
                .accountReference(awazoneUser.getAwazoneUserDomain().getDomainName())
                .getAllAvailableBanks(false)
                .preferredBanks(preferredBanks)
                .customerName(awazoneUser.getFullName())
                .contractCode(CONTRACT_CODE)
                .currencyCode(NGN_CURRENCY_CODE)
                .bvn(bvn)
                .build();
        
        //Pass request to monify
        return monifyAccount(monifyAccountRequest);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Object updateCredentials(CredentialType credentialType, String accountReference, Object credential) {

        if(credentialType.equals(BVN)) {
            String requestUrl = UPDATE_ACCOUNT_BVN_URL + accountReference;
            MonifyBvnUpdate monifyBvnUpdate = MonifyBvnUpdate.builder().bvn((String) credential).build();
            MonifyResponse monifyResponse = sendMonifyRequest(PUT, monifyBvnUpdate, requestUrl, true);

            if(monifyResponse.isRequestSuccessful()) {
                return ((Map<String, String>) monifyResponse.getResponseBody()).get("bvn");
            }
            else {
                throw new VirtualAccountException(monifyResponse.getResponseMessage());
            }
        }
        throw new VirtualAccountException("Unknown credential type");
    }

    @Override
    public Object fetchVirtualAccount(String accountReference) {
        String requestUrl = FETCH_VIRTUAL_ACCOUNT_URL + accountReference;
        return sendMonifyRequest(GET, null, requestUrl, true).getResponseBody();
    }

    @Override
    public void deactivateVirtualAccount(String accountReference) {
        MonifyResponse monifyResponse = sendMonifyRequest(
                DELETE,
                null,
                DEACTIVATE_VIRTUAL_ACCOUNT_URL + accountReference,
                true);
        validateResponse(monifyResponse);
    }

    @Override
    public void initiateSingleTransfer(NewTransferRequest newTransferRequest, String reference) {

        MonifyResponse monifyResponse = sendMonifyRequest(
                POST,
                getMonifyTransferRequestSingle(newTransferRequest, reference),
                SINGLE_TRANSFER_URL,
                true);
        validateResponse(monifyResponse);
    }

    @Override
    public AccountTransactionResponse getTransactionsForAccount(String accountReference, int page, int numberOfRecord) {
        String requestUrl = String.format(GET_ACCOUNT_TRANSACTION_DETAIL_WITH_PAGE_SIZE, accountReference, page, numberOfRecord);

        MonifyResponse monifyResponse = sendMonifyRequest(GET, null, requestUrl, true);

        validateResponse(monifyResponse);

        return new ObjectMapper().convertValue(monifyResponse.getResponseBody(), AccountTransactionResponse.class);
    }

    @Override
    public AccountTransactionResponse getTransactionsForAccount(String accountReference) {

        MonifyResponse monifyResponse = sendMonifyRequest(GET, null, GET_ACCOUNT_TRANSACTION_DETAIL, true);
        validateResponse(monifyResponse);

        return new ObjectMapper().convertValue(monifyResponse.getResponseBody(), AccountTransactionResponse.class);
    }

    private void validateResponse(MonifyResponse monifyResponse) {
        if(!monifyResponse.isRequestSuccessful()) {
            throw new VirtualAccountException(monifyResponse.getResponseMessage());
        }
    }

    @Override
    public ResponseMessage acceptCardPayment(NewCardPaymentRequest newCardPaymentRequest) {
        MonifyResponse monifyResponse = sendMonifyRequest(POST, newCardPaymentRequest, PAY_WITH_CARD_URL, true);
        validateResponse(monifyResponse);

        CardPaymentResponse cardPaymentResponse = new ObjectMapper().convertValue(monifyResponse.getResponseBody(), CardPaymentResponse.class);
        return new ResponseMessage("success", OK, Map.of("paymentData", cardPaymentResponse));
    }

    @Override
    public ResponseMessage acceptCryptoPayment(NewCryptoPaymentRequest newCryptoPaymentRequest) {
        return null;
    }

    @Override
    public ResponseMessage paymentStatus(String transactionReference) {

        String requestUrl =
                (transactionReference.contains("_")) ?
                TRANSACTION_STATUS + transactionReference.replace("_", "|") :
                TRANSACTION_STATUS + transactionReference;

        MonifyResponse monifyResponse = sendMonifyRequest(GET, null, requestUrl, true);

        validateResponse(monifyResponse);

        TransactionStatusResponse transactionStatusResponse =
                new ObjectMapper().convertValue(monifyResponse.getResponseBody(), TransactionStatusResponse.class);

        return new ResponseMessage("success", OK, Map.of("status", transactionStatusResponse));
    }

    @Override
    public ResponseMessage getPayment(String paymentReference) {
        return null;
    }

    @Override
    public ResponseMessage initPayment(NewInitPaymentRequest newInitPaymentRequest) {
        NewMonifyPaymentInitRequest newMonifyPaymentInitRequest = buildRequest(newInitPaymentRequest);

        MonifyResponse monifyResponse =
                sendMonifyRequest(POST, newMonifyPaymentInitRequest, INIT_TRANSACTION_URL, true);

        validateResponse(monifyResponse);

        //Get banks available for transfer payments
        List<Bank> allBanksAndUSSDCodes = getAllBanksAndUSSDCodes();

        NewPaymentInitResponse newPaymentInitResponse =
                new ObjectMapper().convertValue(monifyResponse.getResponseBody(), NewPaymentInitResponse.class);
        return new ResponseMessage("success", OK, Map.of("response", newPaymentInitResponse, "banks", allBanksAndUSSDCodes));

    }

    @Override
    public ActivateBvnResponse validateBvn(ActivateAccountWithBvnRequest activateAccountWithBvnRequest) {

        String dateOfBirth = LocalDate.parse(activateAccountWithBvnRequest.getDateOfBirth())
                .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM));

        MonnifyValidateBvnRequest monnifyValidateBvnRequest = MonnifyValidateBvnRequest.builder()
                .dateOfBirth(dateOfBirth)
                .bvn(activateAccountWithBvnRequest.getBvn())
                .name(activateAccountWithBvnRequest.getBanckAccountName())
                .mobileNo(activateAccountWithBvnRequest.getMobileNo())
                .build();

        MonifyResponse monifyResponse = sendMonifyRequest(
                POST,
                monnifyValidateBvnRequest,
                VALIDATE_BVN,
                true);

        return new ObjectMapper().convertValue(monifyResponse.getResponseBody(), ActivateBvnResponse.class);
    }

    @Override
    public ResponseMessage acceptTransferPayment(NewPayWithBankTransferRequest newPayWithBankTransferRequest) {
        MonifyResponse monifyResponse = sendMonifyRequest(POST, newPayWithBankTransferRequest, BANK_TRANSFER_URL, true);

        validateResponse(monifyResponse);
        PaymentInitResponse paymentInitResponse = new ObjectMapper().convertValue(monifyResponse.getResponseBody(), PaymentInitResponse.class);

        return new ResponseMessage("success", OK, Map.of("response", paymentInitResponse));
    }

    @Override
    public List<Bank> getAllBanksAndUSSDCodes() {
        MonifyResponse monifyResponse = sendMonifyRequest(GET, null, GET_BANKS_URL, true);

        validateResponse(monifyResponse);

        Bank[] banks = new ObjectMapper().convertValue(monifyResponse.getResponseBody(), Bank[].class);
        return Arrays.stream(banks).toList();
    }

    @Override
    public void sendSms(SmsRequest smsRequest) {
        sendSmsRequest(smsRequest);
    }

    private NewMonifyPaymentInitRequest buildRequest(NewInitPaymentRequest newInitPaymentRequest) {

        String redirectUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(PAYMENT_REDIRECT_URL)
                .toUriString();

        return NewMonifyPaymentInitRequest.builder()
                .paymentMethods(ALLOWED_PAYMENT_METHODS)
                .paymentDescription(newInitPaymentRequest.getPaymentDescription())
                .paymentReference(UUID.randomUUID().toString())
                .amount(newInitPaymentRequest.getAmount())
                .contractCode(CONTRACT_CODE)
                .currencyCode(NGN_CURRENCY_CODE)
                .redirectUrl(redirectUrl)
                .customerEmail(newInitPaymentRequest.getCustomerEmail())
                .customerName(newInitPaymentRequest.getCustomerName())
                .build();
    }

    private MonifyTransferRequestSingle getMonifyTransferRequestSingle(NewTransferRequest newTransferRequest, String reference) {
        return MonifyTransferRequestSingle.builder()
                .destinationBankCode(newTransferRequest.getDestinationBankCode())
                .destinationAccountNumber(newTransferRequest.getDestinationAccountNumber())
                .amount(newTransferRequest.getAmount())
                .currency(newTransferRequest.getCurrency())
                .narration(newTransferRequest.getNarration())
                .reference(reference)
                .sourceAccountNumber(newTransferRequest.getSourceAccountNumber())
                .build();
    }

    private VirtualAccountResponse monifyAccount(MonifyAccountRequest monifyAccountRequest) {
        MonifyResponse monifyResponse = sendMonifyRequest(POST, monifyAccountRequest, CREATE_VIRTUAL_ACCOUNT_URL, true);

        if(monifyResponse.isRequestSuccessful() && monifyResponse.getResponseCode() == 0) {
            Map<String, String> map = (Map<String, String>) monifyResponse.getResponseBody();
            return new ObjectMapper().convertValue(map, VirtualAccountResponse.class);
        }
        else {
            throw new VirtualAccountException(monifyResponse.getResponseMessage());
        }
    }

    private MonifyResponse sendMonifyRequest(HttpMethod httpMethod, Object requestBody, String requestUrl, boolean authorization) {

        Optional<Object> requestBodyOptional = Optional.ofNullable(requestBody);
        try {
            if(requestBodyOptional.isPresent()) {
                if(authorization) {
                    return WebClient.create(MONIFY_BASE_URL)
                            .method(httpMethod)
                            .uri(requestUrl)
                            .header("Authorization", AUTHORIZATION_PREFIX + monifyCredentials.getAccessToken())
                            .bodyValue(requestBodyOptional.get())
                            .accept(APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(MonifyResponse.class)
                            .blockOptional().orElseThrow(() -> new UnsuccessfulRequestException("Could not Connect to Payment processor"));
                }
                else {
                    return WebClient.create(MONIFY_BASE_URL)
                            .method(httpMethod)
                            .uri(requestUrl)
                            .bodyValue(requestBodyOptional.get())
                            .accept(APPLICATION_JSON)
                            .retrieve()
                            .bodyToMono(MonifyResponse.class)
                            .blockOptional().orElseThrow(() -> new UnsuccessfulRequestException("Could not Connect to Payment processor"));
                }

            }
            else {
                if(authorization) {
                    return WebClient.create(MONIFY_BASE_URL)
                            .method(httpMethod)
                            .uri(requestUrl)
                            .header("Authorization", AUTHORIZATION_PREFIX + monifyCredentials.getAccessToken())
                            .retrieve()
                            .bodyToMono(MonifyResponse.class)
                            .blockOptional().orElseThrow(() -> new UnsuccessfulRequestException("Could not Connect to Payment processor"));
                }
                else {
                    return WebClient.create(MONIFY_BASE_URL)
                            .method(httpMethod)
                            .uri(requestUrl)
                            .retrieve()
                            .bodyToMono(MonifyResponse.class)
                            .blockOptional().orElseThrow(() -> new UnsuccessfulRequestException("Could not Connect to Payment processor"));
                }
            }
        } catch (Exception e) {
            String message = e.getMessage();
            if(message.contains("422")) {
                throw new VirtualAccountException("Account exists");
            }
            else throw new VirtualAccountException(message);

        }
    }

    private void sendSmsRequest(SmsRequest smsRequest) {

        try {
            WebClient.create(TermiiConfig.BASE_URL)
                    .method(POST)
                    .uri(TermiiConfig.SEND_SMS_URI)
                    .bodyValue(smsRequest)
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(SmsResponse.class)
                    .blockOptional().orElseThrow(() -> new UnsuccessfulRequestException("Could not Connect to SMS processor"));

        } catch (Exception e) {
            if(e.getMessage().contains("400")) {
                throw new EmailNotSentException("Insufficient balance");
            }
            throw new EmailNotSentException(e.getMessage());
        }
    }

}
