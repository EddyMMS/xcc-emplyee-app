package tech.mms.cos.config;

import com.github.jknack.handlebars.internal.lang3.StringUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import org.jetbrains.annotations.NotNull;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.mms.cos.auth.model.AuthenticatedAccount;
import tech.mms.cos.auth.model.AuthenticatedGithubAccount;
import tech.mms.cos.auth.model.AuthenticatedLocalAppAccount;
import tech.mms.cos.core.auth.JwtService;
import tech.mms.cos.core.auth.account.AccountRepository;
import tech.mms.cos.core.auth.account.model.Account;
import tech.mms.cos.core.auth.account.model.GithubAccount;
import tech.mms.cos.core.auth.account.model.LocalAppAccount;
import tech.mms.cos.core.auth.github.GitHubAuthService;
import tech.mms.cos.core.auth.github.model.GithubUser;

@Component
public class OAuthFilter extends OncePerRequestFilter {

  private final AccountRepository accountRepository;
  private final GitHubAuthService gitHubAuthService;
  private final JwtService jwtService;

  public OAuthFilter(
      AccountRepository accountRepository,
      GitHubAuthService gitHubAuthService,
      JwtService jwtService) {
    this.accountRepository = accountRepository;
    this.gitHubAuthService = gitHubAuthService;
    this.jwtService = jwtService;
  }

  @Override
  protected void doFilterInternal(
      @NotNull HttpServletRequest request,
      @NotNull HttpServletResponse response,
      @NotNull FilterChain filterChain)
      throws ServletException, IOException {

    String authHeader = request.getHeader("Authorization");

    if (authHeader != null) {
      authHeader = authHeader.trim();

      if (StringUtils.startsWithIgnoreCase(authHeader, "Bearer gho_")) {
        handleGithubBearerAuth(authHeader);
      } else if (StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
        handleJwtBearerAuth(authHeader);
      }
    }

    filterChain.doFilter(request, response);
  }

  private void handleGithubBearerAuth(String authHeader) {
    String accessToken = authHeader.substring(7).trim();

    GithubUser userInfo = gitHubAuthService.getUserInfo(accessToken);

    GithubAccount githubAccount =
        accountRepository
            .findGithubAccountByGithubId(userInfo.getId())
            .orElseThrow(() -> new BadCredentialsException("GitHub account not found"));

    AuthenticatedGithubAccount authenticatedAccount =
        new AuthenticatedGithubAccount(githubAccount, accessToken);
    SecurityContextHolder.getContext().setAuthentication(authenticatedAccount);
  }

  private void handleJwtBearerAuth(String authHeader) {
    String jwtToken = authHeader.substring(7).trim();

    Claims claims = jwtService.validateToken(jwtToken);
    String username = claims.getSubject();
    Optional<Account> accountOpt = accountRepository.findByUsername(username);

    if (accountOpt.isEmpty()) {
      throw new BadCredentialsException("");
    }

    Account account = accountOpt.get();

    if (!(account instanceof LocalAppAccount)) {
      throw new BadCredentialsException("");
    }

    LocalAppAccount localAccount = (LocalAppAccount) account;
    AuthenticatedAccount authenticatedAccount = new AuthenticatedLocalAppAccount(localAccount);
    SecurityContextHolder.getContext().setAuthentication(authenticatedAccount);
  }
}
