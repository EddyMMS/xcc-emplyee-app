package tech.mms.cos;

import tech.mms.cos.io.ApplicationConfigReader;
import tech.mms.cos.io.ConsoleInputReader;
import tech.mms.cos.io.OutputWriter;
import tech.mms.cos.model.Employee;
import tech.mms.cos.model.Genders;
import tech.mms.cos.model.Name;
import tech.mms.cos.repository.EmployeeRepository;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Exercises:
 *
 * Make Spring Boot Project (Start spring boot service from main function)
 * - What is a maven (mvn) parent
 * - What are libraries
 * - What is spring-web
 * - What is a controller
 * - What is a port (network)
 * - What is a RestAPI
 * - What is a bean
 * - What is a singleton
 *
 * Create a controller
 * - Return "Test" String from a controller. Called over Postman
 * - Return a dummy employee
 *
 * ----
 *
 * ServiceLocator
 * - Remove the ServiceLocator. How does the ServiceLocator work in Spring?
 *
 * Beans
 * - Make all impls of OutputWriter a Bean. Make ConsoleOutputWriter the primary Bean. (@Primary)
 * - Make all impls of EmployeeRepository a Bean. Make MongoEmployeeRepository the primary Bean.
 * - Make all beans use dependency injection.
 *
 * Configuration
 * - What is application.yaml in spring ?
 * - What is a profile in spring configuration?
 * - Change the Configuration to read from application.yaml
 *
 * MongoDB
 * - Instead of using mongojack for connecting to mongodb use spring boot mongodb library (Füge maven library hinzu)
 * - Make the MongoEmployeeRepository work
 *
 * Error Handling
 * - What is an Exception. What is a Runtimeexception. What is the difference? What is a Throwable?
 * - Create a controller Advice. What is it, what does it do, what happens if we dont use it? -> Make the dummy employee returned by the controller be too young to work, meaning an Exception is thrown -> Call this endpoint with Postman. What happens?
 * - Return a nice ZalandoProblem to the api consumer if an Throwable is thrown (Maven Dependency hinzufügen)
 *
 * Logging
 * - Use Log4J & Logback. What is Log4J, what is Logback?
 * - Configure Logback to format logging output differently (choose any format you want, make the time be in red)
 *
 */

public class Application {

    private static OutputWriter output;
    private static ConsoleInputReader reader;

    public static void main(String[] args) {

        if (args.length == 1) {
            String configFileName = args[0];
            ApplicationConfigReader.setConfigFilePath(configFileName);
        }

        ApplicationConfigReader.getConfig();

        output = ServiceLocator.getInstance().get("OutputWriter");
        reader = new ConsoleInputReader();


        // TODO: Wenn ein attribute null ist, dann nicht in json mitschreiben

        EmployeeRepository employeeRepository = ServiceLocator.getInstance().get("EmployeeRepository");
        TopEmployees topEmployees = new TopEmployees(employeeRepository);

        output.println("Hi! Please enter one of the following commands: NEW ACC / LIST / TOP <NUMBER> (Default 5) / READ <NAME>");


        String input = reader.readLine();
        String employeeName = ""; // TODO

        if (input.equals("LIST")) {

            List<Employee> employeeList = employeeRepository.readEmployees();
            employeeList.forEach(employee -> {
                output.println(employee);
            });

        } else if (input.equals("NEW ACC")) {
            Employee employee = createNewEmployee();
            employeeRepository.saveEmployee(employee);
            printEmployee(employee);

        } else if (input.contains("TOP")) {
            int defaultTopEarning = 5;
            output.println(topEmployees.getTopEarningEmployees(defaultTopEarning));

        } else if (input.equals("READ " + String.valueOf(employeeName))) {


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

