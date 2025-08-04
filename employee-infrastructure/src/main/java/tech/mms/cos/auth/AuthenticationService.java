package tech.mms.cos.auth;

import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import tech.mms.cos.auth.model.AuthenticatedAccount;

public class AuthenticationService {

  public static AuthenticatedAccount getAccountAuth() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

    if (!(authentication instanceof AuthenticatedAccount auth)) {
      throw new AuthorizationDeniedException("");
    }

    return auth;
  }
}
