import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import tech.mms.cos.core.EmployeeFunctions;
import tech.mms.cos.core.model.Employee;
import tech.mms.cos.core.model.Genders;
import java.time.LocalDate;


/*
TODO: ./mvnw clean install -> als githup action (workflow) schreiben (.github/workflows/build.yaml)
 */

public class EmployeeFunctionsIT extends AbstractIntegrationTest {

    //TODO: Integration Test implementieren für EmployeeFunctions
    /* do:
     * -> Keine Mocks! Füge Employees in die Datenbank vor dem Test und leere die Datenbank nach den Tests.
     * -> flapdoodle spring -> Startet eine MondoDB automatisch beim Starten deiner Tests in Memory, löschen nach den Tests. -> Ausschalten der lokalen Datenbank!
     * -> Wiremock. -> Testen des RandomEmployeeGeneratorApi mit Wiremock.
     * -
     */

    @Autowired
    public EmployeeFunctions employeeFunctions;

    private final Employee employee1 = new TestEmployeeProvider.EmployeeBuilder().
            hourlyRate(5.5).
            hoursPerWeek(37).
            birthdate(LocalDate.of(1990, 2, 10)).
            gender(Genders.M).
            build();

    private final Employee employee2 = new TestEmployeeProvider.EmployeeBuilder().
            hourlyRate(10)
            .hoursPerWeek(40).
            birthdate(LocalDate.of(1990, 2, 10)).
            gender(Genders.W).
            build();

    private final Employee employee3 = new TestEmployeeProvider.EmployeeBuilder().
            hourlyRate(12.5).
            hoursPerWeek(35).
            birthdate(LocalDate.of(1990, 2, 10)).
            gender(Genders.M).
            build();

    @Test
    public void testTopEarningsByGender() {
        employeeRepository.saveEmployee(employee1);
        employeeRepository.saveEmployee(employee2);
        employeeRepository.saveEmployee(employee3);

        Employee topEarnedMale = employeeFunctions.topEarningsByGender(Genders.M);
        Employee topEarnedFemale = employeeFunctions.topEarningsByGender(Genders.W);

        Assertions.assertEquals(employee3, topEarnedMale);
        Assertions.assertEquals(employee2, topEarnedFemale);
    }

    @Test
    public void test2() {
        Assertions.assertEquals(0, employeeRepository.readEmployees().size() );
    }

}


