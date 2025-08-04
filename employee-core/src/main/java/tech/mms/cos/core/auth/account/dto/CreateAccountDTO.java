package tech.mms.cos.core.auth.account.dto;

import java.util.List;
import tech.mms.cos.core.auth.account.model.AccountRole;

public class CreateAccountDTO {

  private String username;
  private List<AccountRole> roles;
  private List<String> departments;

  public CreateAccountDTO(String username, List<AccountRole> roles, List<String> departments) {
    this.username = username;
    this.roles = roles;
    this.departments = departments;
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
}
