package tech.mms.cos.core;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import tech.mms.cos.core.model.AuthenticatedAccount;

public class AuthenticationService {

    public static AuthenticatedAccount getAccountAuth() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (!(authentication instanceof AuthenticatedAccount auth)) {
            throw new AuthorizationDeniedException("");
        }

        return auth;
    }

}
