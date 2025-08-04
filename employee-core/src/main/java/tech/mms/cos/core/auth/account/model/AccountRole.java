package tech.mms.cos.core.auth.account.model;

public enum AccountRole implements Comparable<AccountRole> {
  NONE(0),
  VIEWER(10),
  EDITOR(20),
  ADMIN(99);

  public int permissionLevel;

  AccountRole(int permissionLevel) {
    this.permissionLevel = permissionLevel;
  }

  public boolean isHigherThan(AccountRole role) {
    return this.permissionLevel > role.permissionLevel;
  }
}
