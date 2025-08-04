package tech.mms.cos.core.auth.github;

import tech.mms.cos.core.auth.github.model.GithubLoginResponse;
import tech.mms.cos.core.auth.github.model.GithubUser;

public interface GitHubAdapter {

  GithubLoginResponse loginWithGithub(String code);

  GithubUser getUserInfo(String accessToken);
}
