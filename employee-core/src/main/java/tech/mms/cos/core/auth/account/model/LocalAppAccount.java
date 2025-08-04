package tech.mms.cos.core.auth.account.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.security.crypto.password.PasswordEncoder;

@TypeAlias("LocalAppAccount")
public class LocalAppAccount extends Account {

  private String encryptedPassword;

  private LocalAppAccount(
      UUID id,
      String username,
      String encryptedPassword,
      List<AccountRole> roles,
      List<String> departments) {
    super(id, username, roles, departments);

    this.encryptedPassword = encryptedPassword;

    validate();
  }

  public static LocalAppAccount createNewAccount(
      String username, PasswordEncoder passwordEncoder, String clearTextPassword) {
    return new LocalAppAccount(
        UUID.randomUUID(),
        username,
        passwordEncoder.encode(clearTextPassword),
        new ArrayList<>(),
        new ArrayList<>());
  }

  public String getEncryptedPassword() {
    return encryptedPassword;
  }

  public void setEncryptedPassword(String encryptedPassword) {
    this.encryptedPassword = encryptedPassword;
  }

  void validate() {
    validateUsername(this.getUsername());
  }

  private void validateUsername(String username) {
    if (username == null || username.trim().isEmpty()) {
      throw new IllegalArgumentException("Username can't be empty");
    }
    String trimmedUsername = username.trim();
    if (trimmedUsername.length() < 5) {
      throw new IllegalArgumentException("Username has to contain at least 5 characters");
    }
    if (trimmedUsername.length() > 30) {
      throw new IllegalArgumentException("Username can't contain over 30 characters");
    }
  }
}
