package net.awazone.awazoneproject.controller.aibopay;

import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.model.aibopay.transaction.AibopayTransaction;
import net.awazone.awazoneproject.service.serviceInterfaces.aibopay.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/aibopay/transaction")
public class TransactionController {

    private final TransactionService transactionService;

    @PreAuthorize("hasAnyAuthority('ROLE_SUPER:ADMIN', 'ROLE_VIEW_ALL_TRANSACTION')")
    @GetMapping(path = "/get/all/{page}")
    public ResponseEntity<ResponseMessage> getAllTransactions(@PathVariable @NotBlank int page) {
        return ResponseEntity.ok(transactionService.findAll(page));
    }

    @PreAuthorize("hasAnyAuthority('ROLE_SUPER:ADMIN', 'ROLE_VIEW_TRANSACTION')")
    @GetMapping(path = "/get")
    public ResponseEntity<AibopayTransaction> getTransaction(@RequestParam @NotBlank String transactionId) {
        return ResponseEntity.ok(transactionService.findByTransactionId(transactionId));
    }
}
