package net.awazone.awazoneproject.controller.aibopay;

import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.model.dtos.aibopay.ActivateAccountWithBvnRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.NewAccountRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.NewDeactivateAccountRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.UpdateBvnRequest;
import net.awazone.awazoneproject.service.serviceInterfaces.aibopay.AccountService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;



import static org.springframework.http.HttpStatus.CREATED;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/api/v1/aibopay/account")
public class AccountController {

    private final AccountService accountService;

    @PreAuthorize("hasAnyAuthority('ROLE_CREATE_NEW_ACCOUNT', 'ROLE_USER', 'ROLE_SUPER:ADMIN')")
    @PostMapping(path = "/new/{userId}")
    public ResponseEntity<ResponseMessage> createNewAccount(@PathVariable("userId") Long userId,
                                                            @Valid @RequestBody NewAccountRequest newAccountRequest) {
        accountService.newAccount(userId, newAccountRequest);
        return ResponseEntity.status(CREATED).body(new ResponseMessage("New account created", CREATED, null));
    }

    @GetMapping(path = "/get/all/{page}")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER:ADMIN', 'ROLE_VIEW_ALL_ACCOUNT')")
    public ResponseEntity<ResponseMessage> getAccounts(@PathVariable @NotBlank Integer page) {
        return accountService.findAllAccounts(page);
    }

    @GetMapping(path = "/get/account")
    @Secured({"ROLE_SUPER:ADMIN","ROLE_USER","ROLE_GET_ONE_ACCOUNT"})
    public ResponseEntity<ResponseMessage> getAccount(@RequestParam("accountNumber") @NotBlank String accountNumber) {
        return ResponseEntity.ok(accountService.findAccountByAccountNumber(accountNumber));
    }

    @PutMapping(path = "/activate/bvn")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER:ADMIN', 'ROLE_USER', 'ROLE_UPDATE_BVN')")
    public ResponseEntity<ResponseMessage> activateWithBvn(@RequestBody @Valid ActivateAccountWithBvnRequest activateAccountWithBvnRequest){
        ResponseMessage responseMessage = accountService.activateWithBvn(activateAccountWithBvnRequest);
        return ResponseEntity.status(responseMessage.getHttpStatus()).body(responseMessage);
    }

    @PutMapping(path = "/update/bvn")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER:ADMIN', 'ROLE_USER', 'ROLE_UPDATE_BVN')")
    public ResponseEntity<ResponseMessage> updateBvn(@RequestBody @Valid UpdateBvnRequest updateBvnRequest){
        return accountService.updateBvn(updateBvnRequest);
    }

    @GetMapping(path = "/get/virtualAccount/{accountReference}")
    @PreAuthorize("hasAnyAuthority('ROLE_SUPER:ADMIN', 'ROLE_GET_VIRTUAL_ACCOUNT')")
    public ResponseEntity<ResponseMessage> getVirtualAccount(@PathVariable("accountReference") @NotBlank String accountReference) {
        return ResponseEntity.ok(accountService.fetchVirtualAccount(accountReference));
    }

    @DeleteMapping(path = "/delete")
    @Secured({"ROLE_SUPER:ADMIN", "ROLE_DELETE_VIRTUAL_ACCOUNT"})
    @PreAuthorize("#newDeactivateAccountRequest.username == authentication.principal")
    public ResponseEntity<ResponseMessage> deleteAccount(@RequestBody @Valid NewDeactivateAccountRequest newDeactivateAccountRequest) {
        return ResponseEntity.ok(accountService.deactivateAccount(newDeactivateAccountRequest));
    }
}
