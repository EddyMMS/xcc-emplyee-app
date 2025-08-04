package tech.mms.cos.core.auth.account.model;

import tech.mms.cos.core.auth.account.dto.CreateAccountDTO;

public interface AccountService {

  LocalAppAccount createAccount(CreateAccountDTO newAccountDTO);
}
