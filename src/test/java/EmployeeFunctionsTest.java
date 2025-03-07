import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static tech.mms.cos.core.model.Genders.M;
import static tech.mms.cos.core.model.Genders.W;

import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.web.client.RestTemplate;
import tech.mms.cos.adapter.random.RandomEmployeeGeneratorApi;
import tech.mms.cos.core.TopEmployees;
import tech.mms.cos.core.model.Employee;
import tech.mms.cos.core.model.Genders;
import tech.mms.cos.core.model.Name;
import tech.mms.cos.repository.EmployeeRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class EmployeeTest {

    EmployeeRepository employeeRepository = mock();
    TopEmployees topEmployees = new TopEmployees(employeeRepository);
    RandomEmployeeGeneratorApi randomEmployeeGeneratorApi = new RandomEmployeeGeneratorApi(new RestTemplate());

    static Employee provideEmployee = new Employee(
            java.time.LocalDate.of(1989, 12, 1),
            5.5,
            37,
            M,
            new Name("John", "Bob", "Man"));

    private List<Employee> provideEmployeeList() {

        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee(
                java.time.LocalDate.of(1990, 2, 10),
                5.5,
                37,
                M,
                new Name("John", "Bob", "Man")));

        employees.add(new Employee(
                java.time.LocalDate.of(1995, 2, 10),
                10,
                40,
                W,
                new Name("Jana", "Job", "Woman")));

        employees.add(new Employee(
                java.time.LocalDate.of(2000, 2, 10),
                12.5,
                35,
                M,
                new Name("Johnny", "Bobby", "Manny")));

        return employees;
    }

    @Test
    public void testGetAverageAge() {
        List<Employee> employeeList = provideEmployeeList();

        double averageAge = topEmployees.getAverageAge(employeeList);

        assertEquals(30.0, averageAge);
    }

    @Test
    public void testGetAverageAgeIfListIsEmpty() {
        double averageAge = topEmployees.getAverageAge(List.of());
        assertEquals(0, averageAge);
    }


    @Test
    public void testTopEarningsByGender(){
        var employee1 = new TestEmployeeProvider.EmployeeBuilder().hourlyRate(5.5).hoursPerWeek(37).gender(M).build();
        var employee2 = new TestEmployeeProvider.EmployeeBuilder().hourlyRate(10).hoursPerWeek(40).gender(W).build();
        var employee3 = new TestEmployeeProvider.EmployeeBuilder().hourlyRate(12.5).hoursPerWeek(37).gender(M).build();

        when(employeeRepository.readEmployees()).thenReturn(List.of(employee1, employee2, employee3));

        Employee topEarnedMale = topEmployees.topEarningsByGender(M);
        Employee topEarnedFemale = topEmployees.topEarningsByGender(W);

        assertEquals(employee3, topEarnedMale);
        assertEquals(employee2, topEarnedFemale);
        assertEquals(employee2.getUuid(), topEarnedFemale.getUuid());

    }

    @Test
    public void testTopEarningEmployees(){
        var employee1 = new TestEmployeeProvider.EmployeeBuilder().hourlyRate(5.5).hoursPerWeek(37).gender(M).build();
        var employee2 = new TestEmployeeProvider.EmployeeBuilder().hourlyRate(10).hoursPerWeek(40).gender(W).build();
        var employee3 = new TestEmployeeProvider.EmployeeBuilder().hourlyRate(12.5).hoursPerWeek(37).gender(M).build();

        when(employeeRepository.readEmployees()).thenReturn(Arrays.asList(employee1, employee2, employee3));

        List<Employee> topEarner = topEmployees.getTopEarningEmployees(20);

        assertEquals(3, topEarner.size());
        assertEquals(employee3, topEarner.get(0));
        assertEquals(employee2, topEarner.get(1));

    }

    static Stream<Arguments> employeeDataProvider() {
        var employee1 = new TestEmployeeProvider.EmployeeBuilder().hourlyRate(5.5).hoursPerWeek(37).gender(M).build();
        var employee2 = new TestEmployeeProvider.EmployeeBuilder().hourlyRate(10.0).hoursPerWeek(40).gender(W).build();
        var employee3 = new TestEmployeeProvider.EmployeeBuilder().hourlyRate(12.5).hoursPerWeek(37).gender(M).build();
        List<Employee> input = Arrays.asList(employee1, employee2, employee3);

        return Stream.of(
                Arguments.of(input, 2, List.of(employee3, employee2)),
                Arguments.of(input, 0, List.of()),
                Arguments.of(input, -1, List.of()),
                Arguments.of(input, 10, List.of(employee3, employee2, employee1))
        );
    }

    @ParameterizedTest
    @MethodSource("employeeDataProvider")
    public void testTopEarningEmployees(List<Employee> input, int n, List<Employee> expectedOutput) {

        when(employeeRepository.readEmployees()).thenReturn(input);

        List<Employee> topEarner = topEmployees.getTopEarningEmployees(n);

        assertEquals(expectedOutput.size(), topEarner.size());
        assertEquals(expectedOutput, topEarner);

    }

    @Test
    public void testAvgSalByGen(){
        var employee1 = new TestEmployeeProvider.EmployeeBuilder().hourlyRate(5.5).hoursPerWeek(37).gender(M).build();
        var employee2 = new TestEmployeeProvider.EmployeeBuilder().hourlyRate(10).hoursPerWeek(40).gender(W).build();
        var employee3 = new TestEmployeeProvider.EmployeeBuilder().hourlyRate(12.5).hoursPerWeek(37).gender(M).build();

        List<Employee> employees = Arrays.asList(employee1, employee2, employee3);

        when(employeeRepository.readEmployees()).thenReturn(employees);

        double avgSalaryByMale = topEmployees.avgSalByGen(M);
        double avgSalaryByFemale = topEmployees.avgSalByGen(W);

        assertEquals(1332.0, avgSalaryByMale);
        assertEquals(1600.0, avgSalaryByFemale);
    }

    @Test
    public void testAvgSalbyAge(){

        var employee1 = new TestEmployeeProvider.EmployeeBuilder().
                hourlyRate(5.5).
                hoursPerWeek(37).
                birthdate(LocalDate.of(1990, 2, 10)).
                build();

        var employee2 = new TestEmployeeProvider.EmployeeBuilder().
                hourlyRate(10)
                .hoursPerWeek(40).
                birthdate(LocalDate.of(1990, 2, 10)).
                build();

        var employee3 = new TestEmployeeProvider.EmployeeBuilder().
                hourlyRate(10).
                hoursPerWeek(40).
                birthdate(LocalDate.of(1990, 2, 10)).
                build();

        List<Employee> employees = Arrays.asList(employee1, employee2, employee3);

        when(employeeRepository.readEmployees()).thenReturn(employees);

        double avgSalarybyAge = topEmployees.avgSalbyAge(35);

        assertEquals(1338.0, avgSalarybyAge);
    }

    @Test
    public void testEmployeeRepositoryIsFilled() {
        List<Employee> employees = provideEmployeeList();
        assertFalse(employees.isEmpty(), "Employee list should not be empty");
    }

    @Test
    public void testProvideEmployeeListLength(){
        List<Employee> employees = provideEmployeeList();
        assertEquals(3, employees.size(), "expected to have 3 employees in the list");
    }

    @ParameterizedTest
    @MethodSource("provideAgeData")
    public void testAverageAge(List Employeee, double expectedAge) {
        when(employeeRepository.readEmployees()).thenReturn(provideEmployeeList());

        double actual = topEmployees.getAverageAge();

        assertEquals(expectedAge, actual);
    }

    private static Stream<Arguments> provideAgeData() {
        return Stream.of(
                Arguments.of(List.of(), 0.0),
                Arguments.of(List.of(provideEmployee), 35.0),
                Arguments.of(2000, 25)
        );
    }

}
