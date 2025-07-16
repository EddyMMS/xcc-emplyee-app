package tech.mms.cos.core.auth.model;

import tech.mms.cos.core.model.GithubAccount;

public class GithubLoginResponse {

    private GithubAccount githubAccount;
    private String accessToken;

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
