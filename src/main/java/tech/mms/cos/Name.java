package tech.mms.cos;

import com.fasterxml.jackson.annotation.JsonIgnore;

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

    private Name(){

    }

    public void validate() {
        if(getFirstName() == null || getFirstName().isBlank()) {
            throw new RuntimeException("First Name is not valid!");
        }
        if(getMiddleName() != null && getMiddleName().isBlank()) {
            throw new RuntimeException("Middle Name is not valid!");
        }
        if(getLastName() == null || getLastName().isBlank()) {
            throw new RuntimeException("Last Name is not valid!");
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
    public String getFullName(){
        if (getMiddleName() == null) {
            return getLastName() + ", " + getFirstName();
        }

        return getLastName() + ", " + getFirstName() + " " + getMiddleName();
    }

}
