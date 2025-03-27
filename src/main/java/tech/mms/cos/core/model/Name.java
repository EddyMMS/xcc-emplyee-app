package tech.mms.cos.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import tech.mms.cos.exception.AppValidationException;

import java.util.Objects;

public class Name {
    private String firstName;
    private String middleName;
    private String lastName;

    public Name(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;

        this.validate();
    }

    private Name() {

    }

    public void validate() {
        if (getFirstName() == null || getFirstName().isBlank()) {
            throw new AppValidationException(NameErrorMessage.firstNameIsNotValid);
        }
        if (getMiddleName() != null && getMiddleName().isBlank()) {
            throw new AppValidationException(NameErrorMessage.middleNameIsNotValid);
        }
        if (getLastName() == null || getLastName().isBlank()) {
            throw new AppValidationException(NameErrorMessage.lastNameIsNotValid);
        }
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    @JsonIgnore
    public String getFullName() {
        if (getMiddleName() == null) {
            return getLastName() + ", " + getFirstName();
        }

        return getLastName() + ", " + getFirstName() + " " + getMiddleName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Name name)) return false;
        return Objects.equals(firstName, name.firstName) && Objects.equals(middleName, name.middleName) && Objects.equals(lastName, name.lastName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, middleName, lastName);
    }
}
