package tech.mms.cos.core.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.data.annotation.Id;
import tech.mms.cos.exception.AppValidationException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Objects;
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
    private String department;


    public Employee(LocalDate birthdate, double hourlyRate, int hoursPerWeek, Genders gender, Name name, String department) {
        this.birthdate = birthdate;
        this.hourlyRate = hourlyRate;
        this.hoursPerWeek = hoursPerWeek;
        this.gender = gender;
        this.name = name;
        this.department = department;
        this.uuid = UUID.randomUUID().toString();

        validate();
    }

    private Employee() {

    }

    @Override
    public String toString() {
        return getFullName() + "\t |" + getAge() + "yo\t |" + getGender();
    }


    public static int MINIMUM_AGE = 14;

    public void validate() throws AppValidationException {
        this.name.validate();

        if (getHoursPerWeek() < 0) {
            throw new AppValidationException(EmployeeErrorMessage.hoursPerWeekIsNegativ);
        }
        if (getHourlyRate() < 0) {
            throw new AppValidationException(EmployeeErrorMessage.hourlyRateIsNegative);
        }
        if (birthdate.isAfter(LocalDate.now())) {
            throw new AppValidationException(EmployeeErrorMessage.birthdateIsInFuture);
        } else if (this.getAge() < MINIMUM_AGE) {
            throw new AppValidationException(EmployeeErrorMessage.employeeTooYoung);
        }
    }


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

    public String getDepartment() {
        return department;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employee employee)) return false;
        return Objects.equals(uuid, employee.uuid);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(uuid);
    }
}

