package tech.mms.cos.core.model;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public abstract class AuthenticatedAccount implements Authentication {

    private final Account account;
    private final List<SimpleGrantedAuthority> authorities;
    private boolean isAuthenticated = true;

    public AuthenticatedAccount(Account account) {
        this.account = account;
        this.authorities = account.getRoles().stream().map(it -> new SimpleGrantedAuthority( "ROLE_" + it.name())).toList();
    }

    public boolean hasHigherPermissionThan(AccountRole role) {
        return getHighestPermission().isHigherThan(role);
    }

    public AccountRole getHighestPermission() {
        return this.account.getHighestRole();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public abstract String getCredentials();

    @Override
    public Account getDetails() {
        return account;
    }

    @Override
    public String getPrincipal() {
        return account.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    @Override
    public String getName() {
        return account.getUsername();
    }

    public boolean hasMinPermission(AccountRole accountRole) {
        return !accountRole.isHigherThan(getHighestPermission());
    }

    public boolean hasLowerPermission(AccountRole accountRole) {
        return accountRole.isHigherThan(getHighestPermission());
    }

    public boolean isBetween(AccountRole min, AccountRole max) {
        return this.hasMinPermission(min) && this.hasLowerPermission(max);
    }
}
