package tech.mms.cos.auth.github.model.adapter;

public class AuthorizationCodeRequest {

  private String client_id;
  private String client_secret;
  private String code;

  public AuthorizationCodeRequest(String clientId, String clientSecret, String code) {
    this.client_id = clientId;
    this.client_secret = clientSecret;
    this.code = code;
  }

  public String getClientId() {
    return client_id;
  }

  public String getClientSecret() {
    return client_secret;
  }

  public String getCode() {
    return code;
  }

  public void setClientId(String clientId) {
    this.client_id = clientId;
  }

  public void setClientSecret(String clientSecret) {
    this.client_secret = clientSecret;
  }

  public void setCode(String code) {
    this.code = code;
  }
}
