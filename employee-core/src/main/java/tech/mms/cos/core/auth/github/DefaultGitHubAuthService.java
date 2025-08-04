package tech.mms.cos.core.auth.github;

import org.springframework.stereotype.Service;
import tech.mms.cos.core.auth.github.model.GithubLoginResponse;
import tech.mms.cos.core.auth.github.model.GithubUser;

@Service
public class DefaultGitHubAuthService implements GitHubAuthService {

  private final GitHubAdapter gitHubAdapter;

  public DefaultGitHubAuthService(GitHubAdapter gitHubAdapter) {
    this.gitHubAdapter = gitHubAdapter;
  }

  @Override
  public GithubLoginResponse loginWithGithub(String code) {
    return gitHubAdapter.loginWithGithub(code);
  }

  @Override
  public GithubUser getUserInfo(String accessToken) {
    return gitHubAdapter.getUserInfo(accessToken);
  }
}
