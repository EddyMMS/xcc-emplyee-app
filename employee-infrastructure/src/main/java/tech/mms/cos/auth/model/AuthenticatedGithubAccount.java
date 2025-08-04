package tech.mms.cos.auth.model;

import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import tech.mms.cos.core.auth.account.model.GithubAccount;

public class AuthenticatedGithubAccount implements Authentication {

  private final GithubAccount githubAccount;
  private final String accessToken;
  private boolean authenticated = true;

  public AuthenticatedGithubAccount(GithubAccount githubAccount, String accessToken) {
    this.githubAccount = githubAccount;
    this.accessToken = accessToken;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return githubAccount.getRoles().stream()
        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
        .collect(Collectors.toList());
  }

  @Override
  public Object getCredentials() {
    return accessToken;
  }

  @Override
  public Object getDetails() {
    return githubAccount;
  }

  @Override
  public Object getPrincipal() {
    return githubAccount.getUsername();
  }

  @Override
  public boolean isAuthenticated() {
    return authenticated;
  }

  @Override
  public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
    this.authenticated = isAuthenticated;
  }

  @Override
  public String getName() {
    return githubAccount.getUsername();
  }
}
