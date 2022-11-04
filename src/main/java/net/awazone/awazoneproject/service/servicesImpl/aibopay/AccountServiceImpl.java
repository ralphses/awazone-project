package net.awazone.awazoneproject.service.servicesImpl.aibopay;

import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.exception.ResourceAlreadyExistException;
import net.awazone.awazoneproject.exception.ResourceNotFoundException;
import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.exception.VirtualAccountException;
import net.awazone.awazoneproject.model.aibopay.account.AibopayAccount;
import net.awazone.awazoneproject.model.dtos.aibopay.ActivateAccountWithBvnRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.NewAccountRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.NewDeactivateAccountRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.UpdateBvnRequest;
import net.awazone.awazoneproject.model.response.ActivateBvnResponse;
import net.awazone.awazoneproject.model.response.VirtualAccountResponse;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.repository.aibopay.AccountRepository;
import net.awazone.awazoneproject.service.serviceInterfaces.aibopay.AccountService;
import net.awazone.awazoneproject.service.serviceInterfaces.aibopay.PaymentProcessor;
import net.awazone.awazoneproject.service.serviceInterfaces.user.AwazoneUserService;
import net.awazone.awazoneproject.service.serviceInterfaces.user.NotificationService;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;

import static java.math.BigDecimal.valueOf;
import static net.awazone.awazoneproject.model.aibopay.account.AccountStatus.*;
import static net.awazone.awazoneproject.model.aibopay.account.AccountStatus.INACTIVE;
import static net.awazone.awazoneproject.model.aibopay.account.AccountType.SAVINGS;
import static net.awazone.awazoneproject.model.user.notification.NotificationPurpose.NEW_AIBOPAY_ACCOUNT;
import static net.awazone.awazoneproject.model.user.notification.NotificationType.SMS;
import static net.awazone.awazoneproject.service.servicesImpl.aibopay.CredentialType.BVN;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final NotificationService notificationService;
    private final PasswordEncoder passwordEncoder;
    private final AwazoneUserService awazoneUserService;
    private final PaymentProcessor paymentProcessor;

    @Override
    public void newAccount(Long userId, NewAccountRequest newAccountRequest) {

        AwazoneUser awazoneUser = findUser(userId);

        //Check if account with user reference already exist
        String accountReference = awazoneUser.getAwazoneUserDomain().getDomainName();
        Optional<AibopayAccount> optionalAibopayAccount = accountRepository.findByAccountReference(accountReference);

        if(optionalAibopayAccount.isPresent()) {
            throw new ResourceAlreadyExistException("Account with reference " + accountReference + " already exist");
        }

        //Create virtual Account
        VirtualAccountResponse virtualAccountResponse = paymentProcessor
                .createVirtualAccount(awazoneUser, newAccountRequest.getBvn(), newAccountRequest.getCustomerFullName());

        //save account to database
        saveNewAccountToDb(virtualAccountResponse);

        //Send notification of account creation to this user
        String message = String.format("""
                        Dear %s,
                        Your new AiboyPay Account was created
                        Account name : %s
                        Account number : %s
                        Bank : %s
                        Welcome to the platform
                        """,
                awazoneUser.getFullName(),
                virtualAccountResponse.getAccountName(),
                virtualAccountResponse.getAccounts()[0].getAccountNumber(),
                virtualAccountResponse.getAccounts()[0].getBankName());

        notificationService.sendNotification(
                awazoneUser,
                "",
                SMS, NEW_AIBOPAY_ACCOUNT,
                "accounts@aibopay.net",
                "New Account",
                message);
    }

    @Override
    public String accountStatus(String accountNumber) {
        AibopayAccount aibopayAccount = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account number " + accountNumber + " not found"));
        return aibopayAccount.getAccountStatus().name();
    }

    @Override
    public AibopayAccount findAccountByReference(String accountReference) {
        return accountRepository.findByAccountReference(accountReference)
                .orElseThrow(() -> new ResourceNotFoundException("Account with reference " + accountReference + " does not exit"));
    }

    @Override
    public ResponseEntity<ResponseMessage> findAllAccounts(Integer page) {
        List<AibopayAccount> allAccounts = accountRepository
                .findAll(PageRequest.of(page - 1, 10))
                .getContent();
        return ResponseEntity.ok(new ResponseMessage("Success", OK, Map.of("accounts", allAccounts)));
    }

    @Override
    public ResponseMessage findAccountByAccountNumber(String accountNumber) {
        AibopayAccount aibopayAccount = getAccount(accountNumber);
        return new ResponseMessage("Success", OK, Map.of("account", aibopayAccount));
    }

    @Override
    public AibopayAccount getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account with account number " + accountNumber + " does not exist"));
    }

    @Override
    public ResponseEntity<ResponseMessage> updateBvn(UpdateBvnRequest updateBvnRequest) {

        AwazoneUser awazoneUser = awazoneUserService.findAppUserByUsername(updateBvnRequest.getUsername());

        if(passwordEncoder.matches(updateBvnRequest.getPassword(), awazoneUser.getPassword())) {
            AibopayAccount aibopayAccount = (AibopayAccount) Objects.requireNonNull(findAccountByAccountNumber(updateBvnRequest.getAccountNumber())).getResponseBody();

            //update virtual account
            String updatedBvn = (String) paymentProcessor.updateCredentials(BVN, aibopayAccount.getAccountReference(), updateBvnRequest.getBvn());

            //Save to database
            aibopayAccount.setBvn(updatedBvn);
        }
        return ResponseEntity.status(OK).body(new ResponseMessage("Bvn updated successfully", OK, null));
    }

    @Override
    public ResponseMessage fetchVirtualAccount(String accountReference) {
        return new ResponseMessage(
                "success",
                OK,
                Map.of("virtualAccount", paymentProcessor.fetchVirtualAccount(accountReference)));
    }

    @Override
    public ResponseMessage deactivateAccount(NewDeactivateAccountRequest newDeactivateAccountRequest) {
        //Todo: Verify this account belongs to this user
        AwazoneUser awazoneUser = awazoneUserService.findAppUserByUsername(newDeactivateAccountRequest.getUsername());
        if (passwordEncoder.matches(newDeactivateAccountRequest.getPassword(), awazoneUser.getPassword())) {
            //Todo: find this account
            AibopayAccount account = findAccountByReference(newDeactivateAccountRequest.getAccountReference());

            //Todo: Deallocate this account from the payment processor
            paymentProcessor.deactivateVirtualAccount(account.getAccountReference());

            //Todo:Effect change in the DB
            accountRepository.delete(account);
            return new ResponseMessage("Success", OK, null);
        }
        throw new VirtualAccountException("Unauthorized operation");
    }

    @Override
    public ResponseMessage activateWithBvn(ActivateAccountWithBvnRequest activateAccountWithBvnRequest) {

        String accountNumber = activateAccountWithBvnRequest.getAccountNumber();
        AibopayAccount aibopayAccount = getAccount(accountNumber);

        if(!aibopayAccount.getAccountStatus().equals(INACTIVE)) {
            throw new ResourceAlreadyExistException("Account with account number " + accountNumber + " already activated");
        }

        ActivateBvnResponse activateBvnResponse = paymentProcessor.validateBvn(activateAccountWithBvnRequest);

        String matchPercentage = (String) activateBvnResponse.getName().get("matchPercentage");

        if(matchPercentage.equalsIgnoreCase("100") && activateBvnResponse.getMobileNo().equalsIgnoreCase("FULL_MATCH")) {

            aibopayAccount.setAccountStatus(ENABLED);
            aibopayAccount.lessFund(20.0);
            return new ResponseMessage("success", OK, Map.of("status", "activated"));
        }
        else {
            return new ResponseMessage("Failed", BAD_REQUEST,
                    Map.of("message", "One or more provided parameter(s) not valid"));
        }

    }

    private AwazoneUser findUser(Long userId) {
        return awazoneUserService.findAppUserById(userId);
    }

    private void saveNewAccountToDb(VirtualAccountResponse virtualAccount) {

        AibopayAccount newAccount = AibopayAccount.builder()
                .accountNumber(virtualAccount.getAccounts()[0].getAccountNumber())
                .accountStatus(INACTIVE)
                .accountType(SAVINGS)
                .bvn(virtualAccount.getBvn())
                .accountReference(virtualAccount.getAccountReference())
                .currentBalance(valueOf(0.0))
                .createdAt(LocalDateTime.parse(virtualAccount.getCreatedOn().replace(" ", "T")))
                .activatedAt(LocalDateTime.now())
                .customerName(virtualAccount.getCustomerName())
                .build();

        accountRepository.save(newAccount);
    }
}
