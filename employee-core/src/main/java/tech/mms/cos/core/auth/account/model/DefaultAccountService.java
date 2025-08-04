package tech.mms.cos.core.auth.account.model;

import java.util.List;
import java.util.UUID;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.mms.cos.core.auth.account.AccountRepository;
import tech.mms.cos.core.auth.account.dto.CreateAccountDTO;

@Service
public class DefaultAccountService implements AccountService {

  private PasswordEncoder passwordEncoder;
  private AccountRepository accountRepository;

  public DefaultAccountService(
      PasswordEncoder passwordEncoder, AccountRepository accountRepository) {
    this.passwordEncoder = passwordEncoder;
    this.accountRepository = accountRepository;
  }

  @Override
  public LocalAppAccount createAccount(CreateAccountDTO newAccountDTO) {

    var roles = determineRoles(newAccountDTO.getRoles());
    String temporaryPassword = generateTemporaryPassword();

    LocalAppAccount account =
        LocalAppAccount.createNewAccount(
            newAccountDTO.getUsername(), passwordEncoder, temporaryPassword);
    account.setRoles(roles);
    account.setDepartments(newAccountDTO.getDepartments());

    return accountRepository.saveAccount(account);
  }

  private List<AccountRole> determineRoles(List<AccountRole> roles) {
    if (roles == null) {
      return List.of(AccountRole.VIEWER);
    } else if (roles.isEmpty()) {
      return List.of(AccountRole.NONE);
    }

    return roles;
  }

  private String generateTemporaryPassword() {
    return UUID.randomUUID().toString().substring(0, 12);
  }
}
