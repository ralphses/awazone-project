package net.awazone.awazoneproject.controller.aibopay;

import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.model.dtos.aibopay.*;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.NewCardPaymentRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.NewInitPaymentRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.webhook.AccountTransactionNotificationRequest;
import net.awazone.awazoneproject.service.servicesImpl.aibopay.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@RestController
@AllArgsConstructor
@RequestMapping("api/v1/aibopay/payment//initialize")
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/initialize")
    public ResponseEntity<ResponseMessage> initializePayment(@RequestBody @Valid NewInitPaymentRequest newPaymentInitRequest) {
        ResponseMessage responseMessage = paymentService.initPayment(newPaymentInitRequest);
        paymentService.saveTransaction(newPaymentInitRequest, responseMessage);
        return ResponseEntity.ok(responseMessage);
    }

    @PostMapping("/card")
    public ResponseEntity<ResponseMessage> payWithCard(@RequestBody @Valid NewCardPaymentRequest newCardPaymentRequest) {
        return ResponseEntity.ok(paymentService.acceptCardPayment(newCardPaymentRequest));
    }

    @PostMapping("/transfer")
    public ResponseEntity<ResponseMessage> payWithTransfer(@RequestBody @Valid NewPayWithBankTransferRequest newPayWithBankTransferRequest) {
        return ResponseEntity.ok(paymentService.acceptTransferPayment(newPayWithBankTransferRequest));
    }

    @PostMapping(path = "/crypto/{coinType}")
    public ResponseEntity<ResponseMessage> payWithCrypto(
            @PathVariable @NotBlank String coinType,
            @RequestParam @NotBlank String amount) {

        return ResponseEntity.ok(paymentService.acceptCryptoPayment(coinType, amount));
    }

    @GetMapping("/status")
    public ResponseEntity<ResponseMessage> paymentStatus(@RequestParam("reference") String paymentReference,
                                                         @RequestParam(value = "coinType", required = false) String coinType) {
        return ResponseEntity.ok(paymentService.paymentStatus(paymentReference, coinType));
    }

    @GetMapping("/get/payment")
    public ResponseEntity<ResponseMessage> getPayment(@RequestParam("reference") String paymentReference) {
        return ResponseEntity.ok(paymentService.getPayment(paymentReference));
    }

    @GetMapping("/get/all-banks")
    public ResponseEntity<ResponseMessage> getAllBanks() {
        return ResponseEntity.ok(paymentService.getAllBanks());
    }

    @PostMapping(path = "/transaction/notification")
    public ResponseEntity<ResponseMessage> transactionNotification(
            @RequestBody AccountTransactionNotificationRequest accountTransactionNotificationRequest) {
        return ResponseEntity.ok(paymentService.updateTransaction(accountTransactionNotificationRequest));
    }

    @PostMapping(path = "/transaction/pay")
    @PreAuthorize("#transactionRequest.username == authentication.principal")
    public ResponseEntity<ResponseMessage> payTransaction(
            @RequestBody TransactionRequest transactionRequest) {
        return ResponseEntity.ok(paymentService.performTransaction(transactionRequest));
    }


}
