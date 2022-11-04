package net.awazone.awazoneproject.service.serviceInterfaces.aibopay;

import net.awazone.awazoneproject.exception.ResponseMessage;
import net.awazone.awazoneproject.model.aibopay.UserWallet;
import net.awazone.awazoneproject.model.aibopay.account.AibopayAccount;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.NewTransferRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.monnify.ViewAccountStatementRequest;
import net.awazone.awazoneproject.model.user.awazoneUser.AwazoneUser;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;

public interface WalletService {
    ResponseMessage loadWallet(String username);

    UserWallet findByUsername(String username);

    ResponseMessage transferFund(NewTransferRequest newTransferRequest, Authentication authentication);

    ResponseMessage viewAccountStatement(ViewAccountStatementRequest viewAccountStatementRequest, LocalDate startDate, LocalDate endDate, Integer page, Authentication authentication);

    UserWallet createWallet(AwazoneUser awazoneUser, AibopayAccount aibopayAccount);
}
