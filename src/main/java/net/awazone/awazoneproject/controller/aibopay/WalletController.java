package net.awazone.awazoneproject.controller.aibopay;

import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.NewTransferRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.ViewAccountStatementRequest;
import net.awazone.awazoneproject.service.serviceInterfaces.aibopay.WalletService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

@RestController
@AllArgsConstructor
@RequestMapping(path = "api/v1/aibopay/wallet")
public class WalletController {

    private final WalletService walletService;

    @Secured({"ROLE_SUPER:ADMIN", "ROLE_VIEW_USER_WALLET"})
    @PreAuthorize("#username == authentication.principal")
    @GetMapping(path = "/get/all")
    public ResponseEntity<ResponseMessage> loadUserWallet(@RequestParam("username") @NotBlank String username) {
        return ResponseEntity.ok(walletService.loadWallet(username));
    }

    @Secured({"ROLE_SUPER:ADMIN", "ROLE_TRANSFER_FUND"})
    @PreAuthorize("#passwordEncoder.matches(#newTransferRequest.password, authentication.credentials)")
    @PostMapping(path = "/transfer")
    public ResponseEntity<ResponseMessage> transferFund(@RequestBody @Valid NewTransferRequest newTransferRequest, Authentication authentication) {
        return ResponseEntity.ok(walletService.transferFund(newTransferRequest, authentication));
    }

    @Secured({"ROLE_SUPER:ADMIN", "ROLE_VIEW_STATEMENT"})
    @PreAuthorize("#passwordEncoder.matches(#viewAccountStatementRequest.password, authentication.credentials)")
    @GetMapping(path = "/statement/{page}")
    public ResponseEntity<ResponseMessage> viewAccountStatement(@PathVariable Integer page,
                                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
                                                                @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
                                                                @RequestBody @Valid ViewAccountStatementRequest viewAccountStatementRequest,
                                                                Authentication authentication) {
        return ResponseEntity.ok(walletService.viewAccountStatement(viewAccountStatementRequest, startDate, endDate, page, authentication));
    }
}
