package tech.mms.cos.core.auth.account.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "accounts")
public class Account {

  @Id private UUID id;
  @Indexed private String username;

  private List<AccountRole> roles;
  private List<String> departments;

  protected Account(UUID id, String username, List<AccountRole> roles, List<String> departments) {
    this.id = id;
    this.username = username;
    this.roles = !roles.isEmpty() ? roles : new ArrayList<>(List.of(AccountRole.NONE));
    this.departments = departments;
  }

  public AccountRole getHighestRole() {
    AccountRole currentHighest = AccountRole.NONE;
    for (AccountRole role : this.getRoles()) {
      if (role.isHigherThan(currentHighest)) {
        currentHighest = role;
      }
    }

    return currentHighest;
  }

  public UUID getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public List<AccountRole> getRoles() {
    return roles;
  }

  public List<String> getDepartments() {
    return departments;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setRoles(List<AccountRole> roles) {
    this.roles = roles;
  }

  public void setDepartments(List<String> departments) {
    this.departments = departments;
  }
}
