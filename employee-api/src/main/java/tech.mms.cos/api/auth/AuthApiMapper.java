package tech.mms.cos.api.auth;

import org.jetbrains.annotations.NotNull;
import tech.mms.cos.api.model.GitHubOAuthResponseDTO;
import tech.mms.cos.api.model.GitHubOAuthUserResponseDTO;
import tech.mms.cos.api.model.LoginResponseDTO;
import tech.mms.cos.core.auth.account.model.GithubAccount;
import tech.mms.cos.core.auth.account.model.LocalAppAccount;
import tech.mms.cos.core.auth.github.model.GithubLoginResponse;

public class AuthApiMapper {

  public static GitHubOAuthResponseDTO mapGitHubOAuthResponse(
      GithubLoginResponse loginResponse, GithubAccount account) {
    return new GitHubOAuthResponseDTO()
        .token(loginResponse.getAccessToken())
        .user(
            new GitHubOAuthUserResponseDTO()
                .username(account.getUsername())
                .email(account.getEmail())
                .githubId(account.getGithubId()));
  }

  public static @NotNull LoginResponseDTO mapLoginResponse(
      String jwtToken, LocalAppAccount account) {
    return new LoginResponseDTO(
        jwtToken,
        account.getUsername(),
        account.getRoles().stream().map(Enum::name).toList(),
        account.getDepartments());
  }
}
