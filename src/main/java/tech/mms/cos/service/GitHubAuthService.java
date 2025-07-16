package tech.mms.cos.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import tech.mms.cos.config.GithubOAuthConfig;
import tech.mms.cos.core.auth.model.GithubEmailInfoResponse;
import tech.mms.cos.core.auth.model.GithubLoginResponse;
import tech.mms.cos.core.auth.model.GithubUserInfoResponse;
import tech.mms.cos.core.model.GithubAccount;
import tech.mms.cos.repository.account.CustomAccountMongoRepository;
import tech.mms.cos.repository.account.DefaultAccountMongoRepository;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class GitHubAuthService {

    private final RestTemplate restTemplate = new RestTemplate();
    private final GithubOAuthConfig config;
    private final CustomAccountMongoRepository accountRepository;

    public GitHubAuthService(GithubOAuthConfig config, CustomAccountMongoRepository accountRepository) {
        this.config = config;
        this.accountRepository = accountRepository;
        // getEmailForAccessToken(aklshalgahlgha)
    }

    /*

    1. Write Redux Store to local storage and read redux store from local browser storage
    2. Oben Rechts im Frontend entweder Login oder Logout anzeigen als Button (Logout löscht access_token from redux)
    3. Anlegen eines Accounts wenn Account für Github Access Token nicht existiert
        3.1 Anlegen einer Funktion in dieser Klasse, welche für den Access Token Userinformationen von Github holt per API.
            Wir brauchen Username, Email (vlt zwei Endpunkte, dann zwei Funktionen). Hinweiß, schreibe eigene Java Klassen für die Response
        3.2 Machen wir zsm

     */

    public GithubLoginResponse loginWithGithub(String code) {
        String accessToken = exchangeCodeForToken(code);

        GithubUserInfoResponse userInfo = getUserInfo(accessToken);

        GithubAccount githubAccount = accountRepository.findGithubAccountByGithubId(userInfo.getId())
                .orElseGet(() -> {
                    GithubEmailInfoResponse emailInfo = getEmailInfo(accessToken);
                    GithubAccount newGithubAccount = new GithubAccount(
                            UUID.randomUUID(),
                            userInfo.getLogin(),
                            userInfo.getId(),
                            emailInfo.getEmail(),
                            List.of(),
                            List.of()
                    );
                    accountRepository.saveAccount(newGithubAccount);
                    return newGithubAccount;
                });

        return new GithubLoginResponse(accessToken, githubAccount);
    }

        // Return here a GithubAccount + access token (Create new class GithubLoginResponse)
        // In Frontend: Login wieder funktional machen. Abspeichern von Username im Redux Store
        // Oben Rechts Username anzeigen
        // Auth per GithubAccount zulassen
        // -> Frontend sendet mit jedem Request Authorization Header mit Value "Bearer ACCESS_TOKEN"
        // -> Im Backend im BasicAuthFilter.java überprüfen ob Basic Auth -> dann so wie davor, oder Bearer
        // Wenn Bearer, dann ruf getGithubUserInfo auf, hol dir die githubId, hol dir den GithubAccount aus der Datenbank
        // -> Wenn alles klappt erstell eine Klasse AuthenticatedGithubAccount (Klasse musst du noch schreib)
        // und lege sie in den SecurityContext






    public String exchangeCodeForToken(String code) {
        String url = "https://github.com/login/oauth/access_token";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", config.getClientId());
        body.add("client_secret", config.getClientSecret());
        body.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return (String) response.getBody().get("access_token");
        } else {
            throw  new RuntimeException("GitHub OAuth failed");
        }
    }


    public GithubUserInfoResponse getUserInfo(String accessToken) {
        String url = "https://api.github.com/user";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<GithubUserInfoResponse> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                GithubUserInfoResponse.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return response.getBody();
        } else {
            throw new RuntimeException("Failed to fetch GitHub user information");
        }
    }


    public GithubEmailInfoResponse getEmailInfo(String accessToken) {
        String url = "https://api.github.com/user/emails";

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(accessToken);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<GithubEmailInfoResponse[]> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                request,
                GithubEmailInfoResponse[].class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            var body = response.getBody();

            if (body.length == 1) {
                return body[0];
            }

            return Arrays.stream(body).filter(GithubEmailInfoResponse::isPrimary).findFirst()
                    .orElse(body[0]);
        } else {
            throw new RuntimeException("Failed to fetch GitHub email information");
        }
    }

}
