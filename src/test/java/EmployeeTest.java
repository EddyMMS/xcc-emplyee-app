import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tech.mms.cos.core.model.Employee;
import tech.mms.cos.core.model.EmployeeErrorMessage;
import tech.mms.cos.exception.AppValidationException;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static tech.mms.cos.core.model.Genders.M;

public class EmployeeTest {

    @Test
    public void testValidate_HoursPerWeekIncorrect() {
        Exception exception = assertThrows(AppValidationException.class, () ->
                new TestEmployeeProvider.EmployeeBuilder().hoursPerWeek(-1).build()
        );

        assertEquals(EmployeeErrorMessage.hoursPerWeekIsNegativ, exception.getMessage());
    }

    @Test
    public void testValidate_HourlyRateIncorrect() {
        Exception exception = assertThrows(AppValidationException.class, () ->
                new TestEmployeeProvider.EmployeeBuilder().hourlyRate(-1).build()
        );

        assertEquals(EmployeeErrorMessage.hourlyRateIsNegative, exception.getMessage());
    }

    @Test
    public void testValidate_BirthdateIncorrect() {
        Exception exception = assertThrows(AppValidationException.class, () ->
                        new TestEmployeeProvider.EmployeeBuilder()
                                .birthdate(LocalDate.now()
                                        .plusYears(5))
                                .build()
               );

        assertEquals(EmployeeErrorMessage.birthdateIsInFuture, exception.getMessage());
    }

    @Test
    public void testValidate_TooYoungToWork() {
        Exception exception = assertThrows(AppValidationException.class, () ->
                        new TestEmployeeProvider
                                .EmployeeBuilder()
                                .birthdate(LocalDate.now()
                                        .minusYears(10))
                                .build()
                );

        assertEquals(EmployeeErrorMessage.employeeTooYoung, exception.getMessage());
    }

    @Test
    public void testValidate_doesNotThrowException() {
        Employee employee = new TestEmployeeProvider.EmployeeBuilder().build();

        assertNotNull(employee);
    }

    @Test
    public void testGetSalary() {
        var employee1 = new TestEmployeeProvider.EmployeeBuilder().hourlyRate(5.5).hoursPerWeek(37).gender(M).build();
        double expectedSalary = employee1.getMonthlySalary();

        assertEquals(814.0, expectedSalary);
    }

    static Stream<Arguments> provideInvalidEmployeeData() {
        return Stream.of(
                Arguments.of(LocalDate.of(1989, 12, 1), 5.5, -1, EmployeeErrorMessage.hoursPerWeekIsNegativ),
                Arguments.of(LocalDate.of(1989, 12, 1), -1, 40, EmployeeErrorMessage.hourlyRateIsNegative),
                Arguments.of(LocalDate.of(2026, 12, 1), 5.5, 40, EmployeeErrorMessage.birthdateIsInFuture),
                Arguments.of(LocalDate.of(2015, 12, 1), 5.5, 40, EmployeeErrorMessage.employeeTooYoung)
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidEmployeeData")
    void testValidate_EmployeeData(LocalDate birthDate, double hourlyRate, int hoursPerWeek, String expectedMessage) {
        Exception exception = assertThrows(AppValidationException.class, () ->

                new TestEmployeeProvider
                        .EmployeeBuilder()
                        .birthdate(birthDate)
                        .hourlyRate(hourlyRate)
                        .hoursPerWeek(hoursPerWeek)
                        .build());

        assertEquals(expectedMessage, exception.getMessage());
    }

}

