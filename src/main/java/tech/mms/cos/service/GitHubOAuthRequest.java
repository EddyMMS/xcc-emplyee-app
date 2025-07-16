package tech.mms.cos.service;

import org.springframework.stereotype.Service;

@Service
public class GitHubOAuthRequest {

    private String code;
    private String state;

    public String getCode() {
        return code;
    }

    public String getState() {
        return state;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setState(String state) {
        this.state = state;
    }
}
