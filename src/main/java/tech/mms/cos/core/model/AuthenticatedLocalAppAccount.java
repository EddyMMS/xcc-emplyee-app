package tech.mms.cos.core.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class AuthenticatedLocalAppAccount extends AuthenticatedAccount {

    private final LocalAppAccount account;

    public AuthenticatedLocalAppAccount(LocalAppAccount account) {
        super(account);

        this.account = account;
    }

    @Override
    public String getCredentials() {
        return account.getEncryptedPassword();
    }

    @Override
    public LocalAppAccount getDetails() {
        return account;
    }

}
