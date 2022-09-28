package net.awazone.awazoneproject.service.servicesImpl.aibopay;

import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.controller.exception.AiboAccountNotFoundException;
import net.awazone.awazoneproject.model.aibopay.account.Account;
import net.awazone.awazoneproject.model.requests.aibopay.NewAccountRequest;
import net.awazone.awazoneproject.model.userService.awazoneUser.AwazoneUser;
import net.awazone.awazoneproject.repository.aibopay.AccountRepository;
import net.awazone.awazoneproject.service.serviceInterfaces.aibopay.AccountService;
import net.awazone.awazoneproject.service.serviceInterfaces.user.AwazoneUserService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Transactional
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AwazoneUserService awazoneUserService;

    @Override
    public void newAccount(Long userId, NewAccountRequest newAccountRequest) {
        AwazoneUser awazoneUser = findUser(userId);

        //Todo: Create virtual Account

        //Todo: save account to database
    }

    @Override
    public String accountStatus(Long accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AiboAccountNotFoundException("Account with id " + " not found"));
        return account.getAccountStatus().name();
    }

    private AwazoneUser findUser(Long userId) {
        return awazoneUserService.findAppUserById(userId);
    }
}
