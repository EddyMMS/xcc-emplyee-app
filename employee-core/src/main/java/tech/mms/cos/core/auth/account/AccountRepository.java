package tech.mms.cos.core.auth.account;

import java.util.Optional;
import tech.mms.cos.core.auth.account.model.Account;
import tech.mms.cos.core.auth.account.model.GithubAccount;

public interface AccountRepository {
  Optional<Account> findByUsername(String username);

  Optional<GithubAccount> findGithubAccountByGithubId(int id);

  <T extends Account> T saveAccount(T account);

  boolean checkPassword(String password, String encryptedPassword);
}
