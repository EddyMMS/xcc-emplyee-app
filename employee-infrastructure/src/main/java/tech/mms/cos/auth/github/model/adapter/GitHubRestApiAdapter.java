package tech.mms.cos.auth.github.model.adapter;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tech.mms.cos.auth.GithubOAuthConfig;
import tech.mms.cos.auth.github.model.GithubEmailInfoResponse;
import tech.mms.cos.auth.github.model.GithubUserInfoResponse;
import tech.mms.cos.core.auth.account.AccountRepository;
import tech.mms.cos.core.auth.account.model.GithubAccount;
import tech.mms.cos.core.auth.github.GitHubAdapter;
import tech.mms.cos.core.auth.github.model.GithubLoginResponse;
import tech.mms.cos.core.auth.github.model.GithubUser;

@Component
public class GitHubRestApiAdapter implements GitHubAdapter {

  private final RestTemplate restTemplate = new RestTemplate();
  private final GithubOAuthConfig config;
  private final AccountRepository accountRepository;

  public GitHubRestApiAdapter(GithubOAuthConfig config, AccountRepository accountRepository) {
    this.config = config;
    this.accountRepository = accountRepository;
  }

  @Override
  public GithubLoginResponse loginWithGithub(String code) {

    String accessToken = exchangeCodeForToken(code);

    GithubUser userInfo = getUserInfo(accessToken);

    GithubAccount githubAccount =
        accountRepository
            .findGithubAccountByGithubId(userInfo.getId())
            .orElseGet(
                () -> {
                  String email = getGithubEmail(accessToken);
                  GithubAccount newGithubAccount =
                      new GithubAccount(
                          UUID.randomUUID(),
                          userInfo.getLogin(),
                          userInfo.getId(),
                          email,
                          List.of(),
                          List.of());
                  accountRepository.saveAccount(newGithubAccount);
                  return newGithubAccount;
                });

    return new GithubLoginResponse(accessToken, githubAccount);
  }

  private String exchangeCodeForToken(String code) {
    String url = "https://github.com/login/oauth/access_token";

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));

    /*uthorizationCodeRequest requestBody = new AuthorizationCodeRequest(
            config.getClientId(),
            config.getClientSecret(),
            code
    );

    Fehler liegt wohl an AuthorizationCodeRequest -> vorerst auskommentiert wg. aktueller Aufgabe

     */

    MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
    requestBody.add("client_id", config.getClientId());
    requestBody.add("client_secret", config.getClientSecret());
    requestBody.add("code", code);

    HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(requestBody, headers);

    ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

    if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
      return (String) response.getBody().get("access_token");
    } else {
      throw new RuntimeException("GitHub OAuth failed");
    }
  }

  @Override
  public GithubUser getUserInfo(String accessToken) {
    String url = "https://api.github.com/user";

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(accessToken);

    HttpEntity<String> request = new HttpEntity<>(headers);

    ResponseEntity<GithubUserInfoResponse> response =
        restTemplate.exchange(url, HttpMethod.GET, request, GithubUserInfoResponse.class);

    if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
      GithubUserInfoResponse body = response.getBody();

      return new GithubUser(body.getId(), body.getName(), body.getLogin());
    } else {
      throw new RuntimeException("Failed to fetch GitHub user information");
    }
  }

  private String getGithubEmail(String accessToken) {

    String url = "https://api.github.com/user/emails";

    HttpHeaders headers = new HttpHeaders();
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    headers.setBearerAuth(accessToken);

    HttpEntity<String> request = new HttpEntity<>(headers);

    ResponseEntity<GithubEmailInfoResponse[]> response =
        restTemplate.exchange(url, HttpMethod.GET, request, GithubEmailInfoResponse[].class);

    if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
      var body = response.getBody();

      if (body.length == 1) {
        return body[0].getEmail();
      }

      return Arrays.stream(body)
          .filter(GithubEmailInfoResponse::isPrimary)
          .findFirst()
          .orElse(body[0])
          .getEmail();
    } else {
      throw new RuntimeException("Failed to fetch GitHub email information");
    }
  }
}
