package tech.mms.cos.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.mms.cos.core.auth.model.GithubLoginResponse;
import tech.mms.cos.core.auth.model.JwtService;
import tech.mms.cos.core.auth.model.LoginRequest;
import tech.mms.cos.core.auth.model.LoginService;
import tech.mms.cos.core.model.GithubAccount;
import tech.mms.cos.core.model.LocalAppAccount;
import tech.mms.cos.exception.LoginFailedException;
import tech.mms.cos.service.GitHubAuthService;
import tech.mms.cos.service.GitHubOAuthRequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private GitHubAuthService gitHubAuthService;
    private LoginService loginService;
    private JwtService jwtService;

    public AuthController(GitHubAuthService gitHubAuthService, LoginService loginService, JwtService jwtService) {
        this.gitHubAuthService = gitHubAuthService;
        this.loginService = loginService;
        this.jwtService = jwtService;
    }

    // 1. Endpunkt der login heißt und im BODY ein username und password hat. Ist POST. Der Endpunkt hat keine Security.
    // 2. Lädst du den User mit dem Username aus der User Datenbank, vergleichst ob Password stimmt, wenn nein 401
    // 3. Erstelle ein JWT Token und schick in zurück (subject = username, issued At, expires At, issuer = XCC, audience = xcc-backend)
    // 4. Ändere der BasicAuthFilter dass das JWT Token akzeptiert.
    //     -> Überprüfen: Ist Abgleaufen? Issuer == XCC? Audience == xcc-backend?
    //     -> Lade Account mit subject-name aus der Datenbank, wenn nicht vorhande 403
    //     -> Lege den user in SecruityContext.

    @PostMapping("/github/access_token")
    public ResponseEntity<Map<String, Object>> exchangeCode(@RequestBody GitHubOAuthRequest request) {
        GithubLoginResponse loginResponse = gitHubAuthService.loginWithGithub(request.getCode());

        GithubAccount account = loginResponse.getGithubAccount();

        Map<String, Object> response = new HashMap<>();
        response.put("token", loginResponse.getAccessToken());
        response.put("user", Map.of(
                "username", account.getUsername(),
                "githubId", account.getGithubId(),
                "email", account.getEmail()
        ));

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();

        Optional<LocalAppAccount> authenticatedUser = loginService.authenticateUser(username, password);

        if (authenticatedUser.isPresent()) {
            LocalAppAccount account = authenticatedUser.get();

            String jwtToken = jwtService.generateToken(account);

            // 2. Aufgabe: Eigene Response Klasse erstellen.
            // 3. Aufgabe: Baue Login in Frontend ein.
            //  3.1 Login Form anpassen -> Setze Token in Redux Store
            //  3.2 (Das funktioniert schon) Testen ob getEmployees im Frontend funktioniert nach login

            // Map ersetzen durch login response klasse
            Map<String, Object> response = new HashMap<>();
            response.put("token", jwtToken);
            response.put("username", account.getUsername());
            response.put("roles", account.getRoles());
            response.put("departments", account.getDepartments());

            return ResponseEntity.ok(response);
        } else {

            // 1. Aufgabe Zalando Problem soll zurückgegeben werden
            throw new LoginFailedException("Credentials do not match");

        }
    }
}
