package tech.mms.cos.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import tech.mms.cos.core.auth.model.JwtService;
import tech.mms.cos.core.model.Account;
import tech.mms.cos.core.model.AuthenticatedAccount;
import tech.mms.cos.core.model.AuthenticatedLocalAppAccount;
import tech.mms.cos.core.model.AuthenticatedGithubAccount;
import tech.mms.cos.core.model.LocalAppAccount;
import tech.mms.cos.core.model.GithubAccount;
import tech.mms.cos.core.auth.model.GithubUserInfoResponse;
import tech.mms.cos.repository.account.CustomAccountMongoRepository;
import tech.mms.cos.service.GitHubAuthService;
import io.jsonwebtoken.Claims;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Component
public class BasicAuthFilter extends OncePerRequestFilter {

    private final CustomAccountMongoRepository customAccountMongoRepository;
    private final GitHubAuthService gitHubAuthService;
    private JwtService jwtService;

    public BasicAuthFilter(CustomAccountMongoRepository customAccountMongoRepository,
                           GitHubAuthService gitHubAuthService,
                           JwtService jwtService) {
        this.customAccountMongoRepository = customAccountMongoRepository;
        this.gitHubAuthService = gitHubAuthService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null) {
            authHeader = authHeader.trim();

            try {
                if (StringUtils.startsWithIgnoreCase(authHeader, "Basic ")) {
                    handleBasicAuth(authHeader);
                } else if (StringUtils.startsWithIgnoreCase(authHeader, "Bearer gho_")) {
                    handleBearerAuth(authHeader);
                } else if (StringUtils.startsWithIgnoreCase(authHeader, "Bearer ")) {
                    handleJwtBearerAuth(authHeader, response);
                }
            } catch (Exception e) {
                if (StringUtils.startsWithIgnoreCase(authHeader, "Bearer ") &&
                        !StringUtils.startsWithIgnoreCase(authHeader, "Bearer gho_")) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private void handleBasicAuth(String authHeader) {

        Pair<String, String> credentials = getUsernameAndPasswordFromBasicAuthHeader(authHeader);
        LocalAppAccount account = getAccountForCredentials(credentials);
        AuthenticatedAccount authenticatedAccount = new AuthenticatedLocalAppAccount(account);
        SecurityContextHolder.getContext().setAuthentication(authenticatedAccount);
    }

    private void handleBearerAuth(String authHeader) {
        String accessToken = authHeader.substring(7).trim();

        GithubUserInfoResponse userInfo = gitHubAuthService.getUserInfo(accessToken);

        GithubAccount githubAccount = customAccountMongoRepository
                .findGithubAccountByGithubId(userInfo.getId())
                .orElseThrow(() -> new BadCredentialsException("GitHub account not found"));

        AuthenticatedGithubAccount authenticatedAccount = new AuthenticatedGithubAccount(githubAccount, accessToken);
        SecurityContextHolder.getContext().setAuthentication(authenticatedAccount);
    }



    private void handleJwtBearerAuth(String authHeader, HttpServletResponse response) throws IOException {
        String jwtToken = authHeader.substring(7).trim();

        Claims claims = jwtService.validateToken(jwtToken);

        String username = claims.getSubject();

        Optional<Account> accountOpt = customAccountMongoRepository.findByUsername(username);

        if (accountOpt.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        Account account = accountOpt.get();

        if (!(account instanceof LocalAppAccount)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        LocalAppAccount localAccount = (LocalAppAccount) account;
        AuthenticatedAccount authenticatedAccount = new AuthenticatedLocalAppAccount(localAccount);
        SecurityContextHolder.getContext().setAuthentication(authenticatedAccount);
    }





    @NotNull
    private Pair<String, String> getUsernameAndPasswordFromBasicAuthHeader(String authHeader) {
        String base64Credentials = authHeader.substring(6).trim();

        try {
            String credentials = new String(Base64.getDecoder().decode(base64Credentials));
            String[] values = credentials.split(":", 2);
            if (values.length != 2) {
                throw new BadCredentialsException("");
            }
            String username = values[0];
            String password = values[1];
            return Pair.of(username, password);
        } catch (IllegalArgumentException e) {
            throw new BadCredentialsException("");
        }
    }

    @NotNull
    private LocalAppAccount getAccountForCredentials(Pair<String, String> credentials) {
        LocalAppAccount account = (LocalAppAccount) customAccountMongoRepository.findByUsername(credentials.getFirst()).orElse(null);
        if (account != null && customAccountMongoRepository.checkPassword(credentials.getSecond(), account.getEncryptedPassword())) {
            return account;
        }
        throw new BadCredentialsException("");
    }
}
