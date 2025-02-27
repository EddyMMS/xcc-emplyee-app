package tech.mms.cos.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import tech.mms.cos.exception.AppValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Employee {

    @Id
    private String uuid;

    private LocalDate birthdate;
    private double hourlyRate;
    private int hoursPerWeek;
    private double monthlySalary;
    private Genders gender;
    private Name name;


    public Employee(LocalDate birthdate, double hourlyRate, int hoursPerWeek, Genders gender, Name name) {
        this.birthdate = birthdate;
        this.hourlyRate = hourlyRate;
        this.hoursPerWeek = hoursPerWeek;
        this.gender = gender;
        this.name = name;
        this.uuid = UUID.randomUUID().toString();

        validate();
    }

    private Employee() {

    }

    @Override
    public String toString() {
        return getFullName() + "\t |" + getAge() + "yo\t |" + getGender();
    }

    public void validate() throws AppValidationException {
        this.name.validate();

        if (getHoursPerWeek() < 0) {
            throw new AppValidationException("Hours per Week is not valid!");
        }
        if (getHourlyRate() < 0) {
            throw new AppValidationException("Hourly Rate is not valid!");
        }
        if (birthdate.isAfter(LocalDate.now())) {
            throw new AppValidationException("The date you entered is in the future! Please enter a valid birthdate!");
        } else if (this.getAge() < 14) {
            throw new AppValidationException("You're too young to work!");
        }
    }

    // TODO: EMPLOYEE TESTEN -> 100 % (sinnvoll)
    // TODO: NAME TESTEN -> 100 % (sinnvoll)

    @JsonIgnore
    public int getAge() {
        LocalDate currentDate = LocalDate.from(LocalDateTime.now());
        return Period.between(birthdate, currentDate).getYears();
    }

    @JsonIgnore
    public int getBirthYear() {
        return birthdate.getYear();
    }

    @JsonIgnore
    public String getFullName() {
        return name.getFullName();
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public int getHoursPerWeek() {
        return hoursPerWeek;
    }

    public double getMonthlySalary() {
        return getHoursPerWeek() * 4 * getHourlyRate();
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public Genders getGender() {
        return gender;
    }

    public Name getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }
}

