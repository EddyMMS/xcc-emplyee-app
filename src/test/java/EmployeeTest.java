import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tech.mms.cos.core.model.Employee;
import tech.mms.cos.core.model.EmployeeErrorMessage;
import tech.mms.cos.core.model.Name;
import tech.mms.cos.exception.AppValidationException;
import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static tech.mms.cos.core.model.Genders.M;

public class EmployeeTest123 {


    LocalDate validBirthdate = LocalDate.of(1990, 12, 10);
    double hourlyRate = 5.5;
    int hoursPerWeek = 10;
    Name name = new Name("John", "Bob", "Man");

    @Test
    public void testValidate_HoursPerWeekIncorrect() {



        Exception exception = assertThrows(AppValidationException.class, () -> new Employee(
                validBirthdate,
                hourlyRate,
                hoursPerWeek = -1,
                M,
                name));


        assertEquals(EmployeeErrorMessage.hoursPerWeekIsNegativ, exception.getMessage());
    }

    @Test
    public void testValidate_HourlyRateIncorrect() {

        Exception exception = assertThrows(AppValidationException.class, () -> new Employee(
                validBirthdate,
                hourlyRate = -1,
                hoursPerWeek,
                M,
                name));

        assertEquals(EmployeeErrorMessage.hourlyRateIsNegative, exception.getMessage());

    }

    @Test
    public void testValidate_BirthdateIncorrect() {

        Exception exception = assertThrows(AppValidationException.class, () -> new Employee(
                LocalDate.now().plusYears(5),
                hourlyRate,
                hoursPerWeek,
                M,
                name));

        assertEquals(EmployeeErrorMessage.birthdateIsInFuture, exception.getMessage());

    }

    @Test
    public void testValidate_TooYoungToWork() {

        Exception exception = assertThrows(AppValidationException.class, () -> new Employee(
                validBirthdate = LocalDate.of(2015, 12, 1),
                hourlyRate,
                hoursPerWeek,
                M,
                name));

        assertEquals(EmployeeErrorMessage.employeeTooYoung, exception.getMessage());

    }

    @Test
    public void testValidate_doesNotThrowException() {

        Employee employee = new Employee(
                validBirthdate,
                hourlyRate,
                hoursPerWeek,
                M,
                name);

        assertNotNull(employee);

    }

    @Test
    public void testGetSalary() {

        var employee1 = new TestEmployeeProvider.EmployeeBuilder().hourlyRate(5.5).hoursPerWeek(37).gender(M).build();
        double expectedSalary = employee1.getMonthlySalary();

        assertEquals(814.0, expectedSalary);

    }
/*

    static Stream<Arguments> provideInvalidEmployeeData() {
        return Stream.of(
                Arguments.of(LocalDate.of(1989, 12, 1), 5.5, -1, "Hours per Week is not valid!"),
                Arguments.of(LocalDate.of(1989, 12, 1), -1, 40, "Hourly Rate is not valid!"),
                Arguments.of(LocalDate.of(2026, 12, 1), 5.5, 40, "The date you entered is in the future! Please enter a valid birthdate!"),
                Arguments.of(LocalDate.of(2015, 12, 1), 5.5, 40, "You're too young to work!")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidEmployeeData")
    void testValidate_EmployeeData(LocalDate birthDate, double hourlyRate, int hoursPerWeek, String expectedMessage) {
        Exception exception = assertThrows(AppValidationException.class, () -> new Employee(
                birthDate,
                hourlyRate,
                hoursPerWeek,
                M,
                new Name("John", "Bob", "Man")));

        assertEquals(expectedMessage, exception.getMessage());
    }
 */



}

