package tech.mms.cos.core.auth;

import java.util.Optional;
import org.springframework.stereotype.Service;
import tech.mms.cos.core.auth.account.AccountRepository;
import tech.mms.cos.core.auth.account.model.Account;
import tech.mms.cos.core.auth.account.model.LocalAppAccount;

@Service
public class LoginService {

  private final AccountRepository accountRepository;

  public LoginService(AccountRepository accountRepository) {
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

    boolean passwordMatches =
        accountRepository.checkPassword(password, localAccount.getEncryptedPassword());

    if (passwordMatches) {
      return Optional.of(localAccount);
    } else {
      return Optional.empty();
    }
  }
}
