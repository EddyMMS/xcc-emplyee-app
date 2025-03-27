
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import tech.mms.cos.core.model.Name;
import tech.mms.cos.core.model.NameErrorMessage;
import tech.mms.cos.exception.AppValidationException;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class NameTest {

    static String validFirstName = "John";
    static String validMiddleName = "Bob";
    static String validLastName = "Man";

    @Test
    public void testValidate_FirstNameIsNull() {

        Exception exception = assertThrows(AppValidationException.class, () -> new Name(
                null, validMiddleName, validLastName
        ));

        assertEquals(NameErrorMessage.firstNameIsNotValid, exception.getMessage());
    }

    @Test
    public void testValidate_FirstNameIsBlank() {

        Exception exception = assertThrows(AppValidationException.class, () -> new Name(
                "", validMiddleName, validLastName
        ));

        assertEquals(NameErrorMessage.firstNameIsNotValid, exception.getMessage());
    }


    @Test
    public void testValidate_MiddleNameIsNotValid() {

        Exception exception = assertThrows(AppValidationException.class, () -> new Name
                (validFirstName, "", validLastName));

        assertEquals(NameErrorMessage.middleNameIsNotValid, exception.getMessage());
    }

    @Test
    public void testValidate_LastNameIsNull() {

        Exception exception = assertThrows(AppValidationException.class, () -> new Name
                (validFirstName, validLastName, null));

        assertEquals(NameErrorMessage.lastNameIsNotValid, exception.getMessage());
    }


    @Test
    public void testValidate_LastNameIsBlank() {
        // Given
        var firstName = "Bernd";
        var middleName = "Sand";
        var blankLastName = "";

        // When
        Exception exception = assertThrows(AppValidationException.class, () -> new Name(firstName, middleName, blankLastName));

        // Then
        assertEquals(NameErrorMessage.lastNameIsNotValid, exception.getMessage());
    }


    @Test
    public void testGetFullName() {
        Name nameWithMiddleName = new Name(validFirstName, validMiddleName, validLastName);
        assertEquals("Man, John Bob", nameWithMiddleName.getFullName());

        Name nameWithoutMiddleName = new Name(validFirstName, null, validLastName);

        assertEquals("Man, John", nameWithoutMiddleName.getFullName());
    }

    static Stream<Arguments> invalidNames() {
        return Stream.of(
                Arguments.of(null, validMiddleName, validLastName, NameErrorMessage.firstNameIsNotValid),
                Arguments.of("", validMiddleName, validLastName, NameErrorMessage.firstNameIsNotValid),
                Arguments.of(validFirstName, "", validLastName, NameErrorMessage.middleNameIsNotValid),
                Arguments.of(validFirstName, validMiddleName, null, NameErrorMessage.lastNameIsNotValid),
                Arguments.of(validFirstName, validMiddleName, "", NameErrorMessage.lastNameIsNotValid)
        );
    }

    @ParameterizedTest
    @MethodSource("invalidNames")
    public void testValidate_Names(String firstName, String middleName, String lastName, String expectedErrorMessage) {
        Exception exception = assertThrows(AppValidationException.class, () -> new Name(firstName, middleName, lastName));
        assertEquals(expectedErrorMessage, exception.getMessage());
    }

}
