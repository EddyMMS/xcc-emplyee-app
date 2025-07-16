package tech.mms.cos.core.auth.model;
import org.springframework.stereotype.Service;
import tech.mms.cos.core.model.Account;
import tech.mms.cos.core.model.LocalAppAccount;
import tech.mms.cos.repository.account.CustomAccountMongoRepository;

import java.util.Optional;

@Service
public class LoginService {

    private final CustomAccountMongoRepository accountRepository;

    public LoginService(CustomAccountMongoRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public Optional<LocalAppAccount> authenticateUser(String username, String password) {

        Optional<Account> accountOpt = accountRepository.findByUsername(username);

        if (accountOpt.isEmpty()) {
            return Optional.empty();
        }

        Account account = accountOpt.get();

        if (!(account instanceof LocalAppAccount)) {
            return Optional.empty();
        }

        LocalAppAccount localAccount = (LocalAppAccount) account;

        boolean passwordMatches = accountRepository.checkPassword(password, localAccount.getEncryptedPassword());

        if (passwordMatches) {
            return Optional.of(localAccount);
        } else {
            return Optional.empty();
        }
    }
}
