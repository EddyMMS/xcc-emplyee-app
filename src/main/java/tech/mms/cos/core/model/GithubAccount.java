package tech.mms.cos.core.model;

import org.springframework.data.annotation.TypeAlias;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@TypeAlias("GithubAccount")
public class GithubAccount extends Account {

    private int githubId;
    private String email;

    public GithubAccount(UUID id, String username, int githubId, String email, List<AccountRole> roles, List<String> departments) {
        super(id, username, roles, departments);

        this.githubId = githubId;
        this.email = email;
    }

    public int getGithubId() {
        return githubId;
    }

    public void setGithubId(int githubId) {
        this.githubId = githubId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
