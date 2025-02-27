import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import tech.mms.cos.core.model.Employee;
import tech.mms.cos.core.model.Name;
import tech.mms.cos.exception.AppValidationException;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static tech.mms.cos.core.model.Genders.M;

public class NameTest {

LocalDate birthdate = LocalDate.of(1990, 12, 10);
double hourlyRate;
int hoursPerWeek;

    @Test
    public void testValidate_FirstNameIsNull(){

        Exception exception = assertThrows(RuntimeException.class, () -> new Employee(
                birthdate, hourlyRate, hoursPerWeek, M,

                new Name(null, "Bob", "Man")));

        assertEquals("First Name is not valid!",exception.getMessage());
    }

    @Test
    public void testValidate_FirstNameIsBlank(){

        Exception exception = assertThrows(RuntimeException.class, () -> new Employee(
                birthdate, hourlyRate, hoursPerWeek, M,

                new Name("", "Bob", "Man")));

        assertEquals("First Name is not valid!",exception.getMessage());
    }


    @Test
    public void testValidate_MiddleNameIsNotValid(){

        Exception exception = assertThrows(RuntimeException.class, () -> new Employee(
                birthdate, hourlyRate, hoursPerWeek, M,


                new Name("John", "", "Man")));

        assertEquals("Middle Name is not valid!",exception.getMessage());
    }

    @Test
    public void testValidate_LastNameIsNull(){

        Exception exception = assertThrows(RuntimeException.class, () -> new Employee(
                birthdate, hourlyRate, hoursPerWeek, M,

                new Name("John", "Bob", null)));

        assertEquals("Last Name is not valid!",exception.getMessage());
    }

    @Test
    public void testValidate_LastNameIsBlank(){

        Exception exception = assertThrows(RuntimeException.class, () -> new Employee(
                birthdate, hourlyRate, hoursPerWeek, M,

                new Name("John", "Bob", "")));

        assertEquals("Last Name is not valid!",exception.getMessage());
    }


@Test
public void testGetFullName(){

    Employee employee = new Employee(
            birthdate, hourlyRate, hoursPerWeek, M,

            new Name("John", "Bob", "Man"));

    assertEquals("Man, John Bob", employee.getFullName());

}

@Test
public void testGetFullNameWithoutMiddleName(){

    Employee employee = new Employee(
            birthdate, hourlyRate, hoursPerWeek, M,

            new Name("John", null, "Man"));

    assertEquals("Man, John", employee.getFullName());

}


}
