package tech.mms.cos.auth.model;

import tech.mms.cos.core.auth.account.model.LocalAppAccount;

public class AuthenticatedLocalAppAccount extends AuthenticatedAccount {

  private final LocalAppAccount account;

  public AuthenticatedLocalAppAccount(LocalAppAccount account) {
    super(account);

    this.account = account;
  }

  @Override
  public String getCredentials() {
    return account.getEncryptedPassword();
  }

  @Override
  public LocalAppAccount getDetails() {
    return account;
  }
}
