package tech.mms.cos.core.auth.github.model;

import tech.mms.cos.core.auth.account.model.GithubAccount;

public class GithubLoginResponse {

  private final GithubAccount githubAccount;
  private final String accessToken;

  public GithubLoginResponse(String accessToken, GithubAccount githubAccount) {
    this.accessToken = accessToken;
    this.githubAccount = githubAccount;
  }

  public GithubAccount getGithubAccount() {
    return githubAccount;
  }

  public String getAccessToken() {
    return accessToken;
  }
}
