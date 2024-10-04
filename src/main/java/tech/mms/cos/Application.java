package tech.mms.cos;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

class Test {
    private String name;
    private int age;

    private Test() {}

    public Test(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }
}

public class Application {

    private static OutputWriter output = new ConsoleOutputWriter();
    private static ConsoleInputReader reader = new ConsoleInputReader();



    public static void main(String[] args) throws JsonProcessingException {

        Test test = new Test("Simon", 26);
        String json = new ObjectMapper().writeValueAsString(test);
        System.out.println(json);
        Test test2 = new ObjectMapper().readValue(json, Test.class);

        // TODO: Wenn ein attribute null ist, dann nicht in json mitschreiben

        EmployeeRepository employeeRepository = new FileEmployeeRepository();

        output.println("Hi! Please enter one of the following commands: NEW ACC / LIST / READ <NAME>");

        if (reader.readLine().equals("LIST")) {

            List<Employee> employeeList = employeeRepository.readEmployees();
            employeeList.forEach(employee -> {
                output.println(employee);
            } );
        } else if (reader.readLine().equals("NEW ACC")){
            Employee employee = createNewEmployee();
            employeeRepository.saveEmployee(employee);
            printEmployee(employee);
    }

    }

    public static Employee createNewEmployee() {


        // TODO: InputReader als eigene Klasse implementieren und Scanner abkapseln

        output.println("What is your full name?");
        String nameInput = reader.readLine();
        String[] nameParts = nameInput.split(" ");

        String firstName;
        String middleName = null;
        String lastName;

        if (nameParts.length == 2) {
            firstName = nameParts[0];
            lastName = nameParts[1];
        } else if (nameParts.length > 2) {
            firstName = nameParts[0];
            middleName = nameParts[1];
            lastName = nameParts[2];
        } else {
            throw new RuntimeException("Please enter at least a firstname and a lastname.");
        }

        Name name = new Name(firstName, middleName, lastName);

        output.println("What is your birthdate? Enter it in the format of dd.MM.yyyy");
        String birthdateInput = reader.readLine();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate birthdate = LocalDate.parse(birthdateInput, formatter);

        output.println("What is your gender? Please enter only one character! M/W/D");
        String genderInput = reader.readLine();
        Genders gender;
        try {
            gender = Genders.valueOf(genderInput);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid input! Please enter only one character! (M/W/D)");
        }


        double hourlyRateInput = -1;
        boolean isInputInvalid = false;
        do {
            try {
                output.println("What is your hourly working rate?");
                hourlyRateInput = reader.readDouble();
            } catch (NumberFormatException e) {
                isInputInvalid = true;
                output.error("Invalid input. Please enter a valid number for the hourly rate");
            }
        } while (isInputInvalid);

        int hoursPerWeekInput;
        while (true) {
            try {
                output.println("How many hours do you work per week?");
                hoursPerWeekInput = reader.readInt();
                break;
            } catch (NumberFormatException e) {
                output.error("Invalid input. Please enter a valid number for the hours per week");
            }
        }

        return new Employee(birthdate, hourlyRateInput, hoursPerWeekInput, gender, name);
    }

    public static void printEmployee(Employee employee) {
        // Single Responsibility Principle
        output.println(employee.getFullName());
        output.println(employee.getAge());
        output.println(employee.getGender());
        output.println(employee.getHourlyRate());
        output.println(employee.getHoursPerWeek());
    }


}

