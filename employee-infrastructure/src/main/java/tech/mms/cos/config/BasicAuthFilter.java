package tech.mms.cos.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Base64;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.mms.cos.auth.model.AuthenticatedAccount;
import tech.mms.cos.auth.model.AuthenticatedLocalAppAccount;
import tech.mms.cos.core.auth.account.AccountRepository;
import tech.mms.cos.core.auth.account.model.LocalAppAccount;

@Component
public class BasicAuthFilter extends OncePerRequestFilter {

  private final AccountRepository accountRepository;

  public BasicAuthFilter(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  protected void doFilterInternal(
      @NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader != null
        && (SecurityContextHolder.getContext().getAuthentication() == null
            || !SecurityContextHolder.getContext().getAuthentication().isAuthenticated())) {
      try {
        handleBasicAuth(authHeader.trim());
      } catch (Exception e) {
        // ignore
      }
    }

    filterChain.doFilter(request, response);
  }

  private void handleBasicAuth(String authHeader) {
    Pair<String, String> credentials = getUsernameAndPasswordFromBasicAuthHeader(authHeader);
    LocalAppAccount account = getAccountForCredentials(credentials);
    AuthenticatedAccount authenticatedAccount = new AuthenticatedLocalAppAccount(account);
    SecurityContextHolder.getContext().setAuthentication(authenticatedAccount);
  }

  @NotNull
  private Pair<String, String> getUsernameAndPasswordFromBasicAuthHeader(String authHeader) {
    String base64Credentials = authHeader.substring(6).trim();

    try {
      String credentials = new String(Base64.getDecoder().decode(base64Credentials));
      String[] values = credentials.split(":", 2);
      if (values.length != 2) {
        throw new BadCredentialsException("");
      }
      String username = values[0];
      String password = values[1];
      return Pair.of(username, password);
    } catch (IllegalArgumentException e) {
      throw new BadCredentialsException("");
    }
  }

  @NotNull
  private LocalAppAccount getAccountForCredentials(Pair<String, String> credentials) {
    LocalAppAccount account =
        (LocalAppAccount) accountRepository.findByUsername(credentials.getFirst()).orElse(null);
    if (account != null
        && accountRepository.checkPassword(
            credentials.getSecond(), account.getEncryptedPassword())) {
      return account;
    } else if (account == null) {
      throw new AuthenticationCredentialsNotFoundException("");
    }
    throw new BadCredentialsException("");
  }
}
