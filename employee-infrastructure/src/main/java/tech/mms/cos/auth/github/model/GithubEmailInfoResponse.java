package tech.mms.cos.auth.github.model;

public class GithubEmailInfoResponse {

  private String email;
  private boolean primary;

  public GithubEmailInfoResponse(String email, boolean primary) {
    this.email = email;
    this.primary = primary;
  }

  public String getEmail() {
    return email;
  }

  public boolean isPrimary() {
    return primary;
  }
}
