package tech.mms.cos.api.auth;

import static tech.mms.cos.api.auth.AuthApiMapper.mapGitHubOAuthResponse;
import static tech.mms.cos.api.auth.AuthApiMapper.mapLoginResponse;

import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import tech.mms.cos.api.client.AuthApi;
import tech.mms.cos.api.model.*;
import tech.mms.cos.core.auth.JwtService;
import tech.mms.cos.core.auth.LoginService;
import tech.mms.cos.core.auth.account.dto.CreateAccountDTO;
import tech.mms.cos.core.auth.account.model.*;
import tech.mms.cos.core.auth.github.GitHubAuthService;
import tech.mms.cos.core.auth.github.model.GithubLoginResponse;
import tech.mms.cos.exception.LoginFailedException;

@RestController
public class AuthController implements AuthApi {

  /*
     Add new endpoint to create an Account (LocalAppAccount)
     1. Add Enpoint to spec yaml /accounts/register POST Body (Username, roles, repartment) mit defaults (Rolle == [] -> Role.NONE, Rolle == null -> VIEWER)
     2. Implement in this controller the endpoint (@Override) + SecurityConfig -> dieser Enpoint nur mit Rolle ADMIN
     3. Anelegen eines AccountService Interfaces + DefaultAccountService class, welche die Anfrage zum Anelegen eines Accounts entgegennimmt
     createAccount(String username, Role[] roles, String[] departments) ODER createAccount(CreateAccountDTO createAccountDto)
     4. Der Service validiert ob at least eine Rolle, username mind 5 Zeichen max 30 Zeichen
     5. Der Service legt einen Account in der Datenbank an -> AccountRepository
     6. Zurückgeben an Controller -> Neuer Account (mit id)
     7. Controller gibt Response an Aufrufer zurück
     8. Testen über Postman

  */

  private GitHubAuthService gitHubAuthService;
  private LoginService loginService;
  private JwtService jwtService;
  private AccountService accountService;

  public AuthController(
      GitHubAuthService gitHubAuthService,
      LoginService loginService,
      JwtService jwtService,
      AccountService accountService) {
    this.gitHubAuthService = gitHubAuthService;
    this.loginService = loginService;
    this.jwtService = jwtService;
    this.accountService = accountService;
  }

  @Override
  public ResponseEntity<LoginResponseDTO> login(LoginRequestDTO loginRequestDTO) {
    String username = loginRequestDTO.getUsername();
    String password = loginRequestDTO.getPassword();

    Optional<LocalAppAccount> authenticatedUser = loginService.authenticateUser(username, password);

    if (authenticatedUser.isPresent()) {
      LocalAppAccount account = authenticatedUser.get();
      String jwtToken = jwtService.generateToken(account);

      return ResponseEntity.ok(mapLoginResponse(jwtToken, account));
    } else {
      throw new LoginFailedException("Credentials do not match");
    }
  }

  @Override
  public ResponseEntity<RegisterResponseDTO> register(RegisterRequestDTO registerRequestDTO) {

    CreateAccountDTO dto =
        new CreateAccountDTO(
            registerRequestDTO.getUsername(),
            registerRequestDTO.getRoles().stream().map(AccountRole::valueOf).toList(),
            registerRequestDTO.getDepartments());

    LocalAppAccount createdAccount = accountService.createAccount(dto);

    return ResponseEntity.ok(
        new RegisterResponseDTO()
            .id(createdAccount.getId().toString())
            .username(createdAccount.getUsername())
            .roles(createdAccount.getRoles().stream().map(Enum::name).toList())
            .departments(createdAccount.getDepartments()));
  }

  @Override
  public ResponseEntity<GitHubOAuthResponseDTO> oAuthLogin(
      GitHubOAuthRequestDTO gitHubOAuthRequestDTO) {
    GithubLoginResponse loginResponse =
        gitHubAuthService.loginWithGithub(gitHubOAuthRequestDTO.getCode());
    GithubAccount account = loginResponse.getGithubAccount();

    GitHubOAuthResponseDTO response = mapGitHubOAuthResponse(loginResponse, account);
    return ResponseEntity.ok(response);
  }
  ;
}
