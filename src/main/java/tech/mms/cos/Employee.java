package tech.mms.cos;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Scanner;
import java.time.format.DateTimeFormatter;

public class Employee {

    static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    String birthdate;
    double hourlyRate;
    int hoursPerWeek;
    String gender;
    String name;

    public int getAge() {
        LocalDate birthdate = LocalDate.parse(this.birthdate, formatter);
        LocalDate currentDate = LocalDate.from(LocalDateTime.now());

        return Period.between(birthdate, currentDate).getYears();
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        System.out.println("Hi! Pleaser answer the questions below!");

        Employee employee = new Employee();


        System.out.println("What is your full name?");
        employee.name = scanner.nextLine();

        String[] nameParts = employee.name.split(" ");

        String firstName;
        String middleName;
        String lastName;

        // TODO: Speichern der namen-teile am Employee
        if (nameParts.length == 2){
            firstName = nameParts[0];
            lastName = nameParts[1];
        } else if (nameParts.length > 2){
            firstName = nameParts[0];
            middleName = nameParts[1];
            lastName = nameParts[2];
        } else {
            System.out.println("Please enter at least a firstname and a lastname.");
            return;
        }

        System.out.println("What is your birthdate? Enter it in the format of dd.MM.yyyy");
        employee.birthdate = scanner.nextLine();

        System.out.println("What is your gender?");
        employee.gender = scanner.nextLine();
        // TODO: Überprüfung auf m/w/d -> Speichern als Enum


        boolean isInputValid = false;
        do {
            try {
                System.out.println("What is your hourly working rate?");
                employee.hourlyRate = Double.parseDouble(scanner.nextLine());
                isInputValid = true;
            } catch (NumberFormatException e){
                System.out.println("Invalid input. Please enter a valid number for the hourly rate");
            }
        } while(!isInputValid);


        while (true) {
            try {
                System.out.println("How many hours do you work per week?");
                employee.hoursPerWeek = Integer.parseInt(scanner.nextLine());
                break;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number for the hours per week");
            }
        }

        // TODO: Auslagerung der main funktion und der print funktion in eine Klasse namens Application
        employee.printEmployee();
    }

    public void printEmployee() {
        // TODO: Ausgabe des Namens als: NACHNAME, VORNAME MITTELNAME

        // Single Responsibility Principle
        System.out.println(name);
        System.out.println(getAge());
        System.out.println(gender);
        System.out.println(hourlyRate);
        System.out.println(hoursPerWeek);
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

