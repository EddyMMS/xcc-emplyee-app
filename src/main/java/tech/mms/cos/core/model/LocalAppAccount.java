package tech.mms.cos.core.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@TypeAlias("LocalAppAccount")
public class LocalAppAccount extends Account {

    private String encryptedPassword;

    private LocalAppAccount(UUID id, String username, String encryptedPassword, List<AccountRole> roles, List<String> departments) {
        super(id, username, roles, departments);

        this.encryptedPassword = encryptedPassword;
    }

    public static LocalAppAccount createNewAccount(String username, PasswordEncoder passwordEncoder, String clearTextPassword) {
        return new LocalAppAccount(
                UUID.randomUUID(),
                username,
                passwordEncoder.encode(clearTextPassword),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }

    public String getEncryptedPassword() {
        return encryptedPassword;
    }

    public void setEncryptedPassword(String encryptedPassword) {
        this.encryptedPassword = encryptedPassword;
    }

}
