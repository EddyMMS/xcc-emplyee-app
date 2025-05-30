package tech.mms.cos;

import tech.mms.cos.io.ConsoleInputReader;
import tech.mms.cos.io.OutputWriter;

/**
 * Exercises:
 *
 * Make Spring Boot Project (Start spring boot service from main function)
 *
 * - What is a maven (mvn) parent
 * --- Dies ist eine Vorlage, von der andere Projekte erben können. (Gemeinsame Konfigurationen und Abhängigkeiten können damit verwaltet werden.)
 *
 * - What are libraries
 * --- Sammlungen von vorgefertigtem Code, den Entwickler verwenden können, um Funktionen zu ihren eigenen Programmen hinzuzufügen
 *
 * - What is spring-web
 * --- ist ein Modul des Spring-Frameworks, das Werkzeuge zum Erstellen von Webanwendungen, einschließlich RESTful-Diensten, bereitstellt.
 *
 * - What is a controller
 * --- Ein Controller ist eine Komponente, die Benutzeranfragen verarbeitet, sie bearbeitet und entsprechende Antwort zurückgibt
 *
 * - What is a port (network)
 * --- Virtueller Punkt, an dem Netzwerkverbindungen beginnen und enden
 *
 * - What is a RestAPI
 * --- Representational State Transfer Application Programming Interface === ermöglicht es verschied. Softwaresystemen über das Internet zu kommunizieren
 *
 * - What is a bean
 *--- Ein Objekt, das vom Spring-Container verwaltet wird (Wird von Spring erstellt und verwaltet)
 *
 * - What is a singleton
 * --- Muster in der Programmierung, dass eine Klasse nur eine Instanz hat und nur einen globalen Zugriffspunkt darauf bietet
 *
 * Create a controller
 * - Return "Test" String from a controller. Called over Postman
 * - Return a getDummy employee
 *
 * ----
 *
 * ServiceLocator
 * - Remove the ServiceLocator. How does the ServiceLocator work in Spring? ( Google: Spring boot dependency injection, how does it work in spring boot, dependency injection constructor injection spring boot, spring boot @Component)
 *
 * Beans
 * - Make all impls of OutputWriter a Bean. Make ConsoleOutputWriter the primary Bean. (@Primary)
 * - Make all impls of EmployeeRepository a Bean. Make MongoEmployeeRepository the primary Bean.
 * - Make all beans use dependency injection.
 *
 *
 * Configuration
 * - What is application.yaml in spring ?
 * --- Das ist eine Konfigurationsdatei in Spring Boot, in welcher man verschiedene EInstellung der App festlegt. (Leicht änderbar)
 *
 * - What is a profile in spring configuration?
 * --- Mit Profilen kann man unterschiedliche Konfigurationen für verschied. Umgebungen verwenden.
 *
 * - Change the Configuration to read from application.yaml
 *
 *
 *  -----------------------
 *
 * MongoDB
 * - Instead of using mongojack for connecting to mongodb use spring boot mongodb library (Füge maven library hinzu)
 * - Make the MongoEmployeeRepository work
 *
 *  -----------------------------
 *
 *  Create all common HTTP Method Controller functions
 *  - "/employees" -> GET => Gibt alle employees zurück ; POST -> Anleigen eines neuen Employees (Employee als Methoden Parameter / body)
 *  - "/employees/{uuid}" -> DELETE => Lösche den Employee mit dieser UUID
 *  - "/employees/{uuid}" -> GET => Returne den Employee mit dieser UUID
 *
 * ---------------
 *
 * Error Handling
 * - What is an Exception. What is a Runtimeexception. What is the difference between Exception and RuntimeException? What is a Throwable?
 * --- Eine Exception signalisiert einen Fehlerzustand, der die normale Ausführung eines Programms unterbricht.
 * --- Eine RuntimeException ist eine spez. Art von Exception, die während der Laufzeit auftreten kann und nicht explizit
 * behandelt werden muss
 * --- Ein Throwable ist die Basisklasse für alle Fehler und Ausnahmen in Java
 *
 * - Rework "validate" function of Employee. Throw new own Exception (neue Klasse) AppValidationException.
 *
 *
 * - Create a controller Advice (Google: @ControllerAdvice). What is it, what does it do, what happens if we dont use it?
 *--- Es ermöglicht zentralisierte Fehlerbehandlung in Spring
 *--- Ohne, müsste jede Controller Klasse eine eigene Fehlerbehandlung definieren
 *
 * - Make ControllerAdvice catch the App Exception and return Bad Request to caller. ResponseEntity<Void>
 *
 * - Return a nice ZalandoProblem to the api consumer if an Throwable is thrown (Maven Dependency hinzufügen)
 * anstatt bad request(leerer body) -> zalandoproblem ResponseEntity<Problem>
 *
 *
 *
 * Logging
 * - Use Log4J & Logback. What is Log4J, what is Logback?
 * - Configure Logback to format logging output differently (choose any format you want, make the time be in red)
 *
 */

/**
 * Write a JUnit test. What is a Unit Test?
 * --- Ein Unit-Test dient dazu, einzelne Teile eines Programms zu testen, damit möchte man
 * einzelne Komponente überprüfen
 *
 * -> getAverageAge(List<Employee> employeeList) -> employeeRepository = null
 * -> Test als Parametrized Test -> https://www.baeldung.com/parameterized-tests-junit-5
 * -> getAverageAge() -> employeeRepository = mock()
 *
 * import static org.mockito.Mockito.*;
 * List<String> mockedList = mock(EmployeeRepostiory.class);
 * when(mockedList.get(0)).thenReturn("Hello, World!");
 * System.out.println(mockedList.get(0)); // Gibt "Hello, World!" aus
 *
 *
 * Write a Spring Boot integration Test. What is a integration test? how it differs from Unit Test?
 * --- Ein Integration Test überprüft die Zusammenarbeit zwischen verschied. Komponenten oder Systemen innerhalb eines Projekts.
 * --- Unit Tests testen die kleinsten Teile eines Programms wie Funktionen oder Methoden und Integration Test testen die Interaktionen zwischen mehreren Komponenten
 *
 * -> getAverage() aber als Integration Test! Hint: Datenbank muss local laufen.
 * Nicht TopEmployees selber erstellen sondern injecten lassen! Warum funktioniert Dependency Injecten hier aber nicht in Unit Tests?
 *--- Dep. Inject. funktioniert idR. schon bei Unit Tests, kann allerdings relativ schnell komplex werden, Injection für Tests aufwändiger usw.
 *
 * -> Keine Mocks! Füge Employees in die Datenbank vor dem Test und leere die Datenbank nach den Tests.
 * -> flapdoodle spring -> Startet eine MondoDB automatisch beim Starten deiner Tests in Memory, löschen nach den Tests. -> Ausschalten der lokalen Datenbank!
 * -> Wiremock. -> Testen des RandomEmployeeGeneratorApi mit Wiremock.
 *
 */

public class Application {

    private static OutputWriter output;
    private static ConsoleInputReader reader;
 /*
    public static void main(String[] args) {

        /*

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


         */
}

