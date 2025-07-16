package tech.mms.cos.core.auth.model;

public class GithubUserInfoResponse {

    private int id;
    private String name;
    private String login;
    private String email;

    public GithubUserInfoResponse(int id, String name, String login, String email) {
        this.id = id;
        this.name = name;
        this.login = login;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getLogin() {
        return login;
    }

    public String getEmail() {
        return email;
    }
}
