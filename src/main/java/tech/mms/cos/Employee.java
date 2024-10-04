package tech.mms.cos;
import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Date;

/*
TODO Constructor hinzufuegen

TODO firstName, middleName, lastName -> Eigene Klasse

TODO Alle Attribute sind private -> Kapselung! Getter!

TODO Inhalt der Variablen beim erstellen des Employees pruefen
    hourlyRate + hoursPerWeek > 0
    firstName, lastName nicht null
    Birthdate darf nicht in Zukunft liegen oder Employee juenger als 14
    Falls irgendwas nicht zutrifft RuntimeException schmeissen
 */

public class Employee {

    private LocalDate birthdate;
    private double hourlyRate;
    private int hoursPerWeek;
    private Genders gender;
    private Name name;

    public Employee(LocalDate birthdate, double hourlyRate, int hoursPerWeek, Genders gender, Name name){
        this.birthdate = birthdate;
        this.hourlyRate = hourlyRate;
        this.hoursPerWeek = hoursPerWeek;
        this.gender = gender;
        this.name = name;

        validate();
    }

    private Employee(){

    }

    @Override
    public String toString() {
        return getFullName() + "\t |" + getAge() + "yo\t |" + getGender();
    }

    private void validate() {
        this.name.validate();
        if(getHoursPerWeek() < 0) {
            throw new RuntimeException("Hours per Week is not valid!");
        }
        if(getHourlyRate() < 0) {
            throw new RuntimeException("Hours per Week is not valid!");
        }
        if (birthdate.isAfter(LocalDate.now())) {
            throw new RuntimeException("The date you entered is in the future! Please enter a valid birthdate!");
        } else if (this.getAge() < 14) {
            throw new RuntimeException("You're too young to work!");
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
    public String getFullName(){
        return name.getFullName();
    }

    public double getHourlyRate(){
        return hourlyRate;
    }

    public int getHoursPerWeek(){
        return hoursPerWeek;
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
}

    /*
        String surname;
        String middlename;
        String lastname;

        int age;
        String gender;
        int hourlyrate;
        int hoursweek;

    public Personal(String surname, String middlename, String lastname, int age, String gender,int hourlyrate,
        int hoursweek){

        this.surname = surname;
        this.middlename = middlename;
        this.lastname = lastname;
        this.age = age;

        if (gender == "m" || gender == "f" || gender == "d") {
            this.gender = gender;
        } else {
            System.out.println("Invalid gender! Please enter only 1 character! (m/f/d)");
        }

        this.hourlyrate = hourlyrate;
        this.hoursweek = hoursweek;


            }

     */

