package tech.mms.cos.api.model;

import tech.mms.cos.core.model.Genders;
import tech.mms.cos.core.model.Name;

import java.time.LocalDate;

public class NewEmployeeRequest {

    private LocalDate birthdate;
    private double hourlyRate;
    private int hoursPerWeek;
    private Genders gender;
    private Name name;

    private NewEmployeeRequest() {

    }

    public NewEmployeeRequest(LocalDate birthdate, double hourlyRate, int hoursPerWeek, Genders gender, Name name) {
        this.birthdate = birthdate;
        this.hourlyRate = hourlyRate;
        this.hoursPerWeek = hoursPerWeek;
        this.gender = gender;
        this.name = name;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public int getHoursPerWeek() {
        return hoursPerWeek;
    }

    public void setHoursPerWeek(int hoursPerWeek) {
        this.hoursPerWeek = hoursPerWeek;
    }

    public Genders getGender() {
        return gender;
    }

    public void setGender(Genders gender) {
        this.gender = gender;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }
}
