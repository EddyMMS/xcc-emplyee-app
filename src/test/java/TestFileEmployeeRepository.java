import tech.mms.cos.repository.Config;
import tech.mms.cos.repository.FileEmployeeRepository;

public class TestFileEmployeeRepository {


    private Config testConfig = new Config("test-employee.json");

    private FileEmployeeRepository employeeRepository = new FileEmployeeRepository(testConfig);

    public void test() {
        if (employeeRepository.readEmployees().size() != 5) {
            throw new RuntimeException("Test failed");
        }
    }

}
