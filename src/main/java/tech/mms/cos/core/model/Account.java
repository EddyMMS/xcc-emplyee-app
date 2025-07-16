package tech.mms.cos.core.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


// TODO: Model eine Klasse mit dem Namen "Account" anelegst. Username und password, roles (list) und departments (list).
// TODO: Laden des Accounts aus der Datenbank beim überprüfen der Auth. (Neues repo)

// TODO: BCryptPasswordEncoder. Was ist ein Password Salt, was ist eine Password rainbow table, wie funktioniert BCryptPasswordEncoder?
// TODO: Password wird in DB verschlüsselt gespeichert und beim überprüfen ob das Password übereinstimmt auch BCryptPasswordEncoder verwenden.

@Document(collection = "accounts")
public class Account {

    @Id private UUID id;
    @Indexed private String username;

    private List<AccountRole> roles;
    private List<String> departments;

    protected Account(UUID id, String username, List<AccountRole> roles, List<String> departments) {
        this.id = id;
        this.username = username;
        this.roles = roles;
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
