package tech.mms.cos;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import tech.mms.cos.io.OutputWriter;
import tech.mms.cos.model.Employee;
import tech.mms.cos.model.Name;
import tech.mms.cos.repository.EmployeeRepository;
import tech.mms.cos.repository.MongoEmployeeRepository;

import java.time.LocalDate;
import java.util.List;

import static tech.mms.cos.model.Genders.M;

@RestController
public class SpringController {

    private final EmployeeRepository employeeRepository;

    public SpringController(EmployeeRepository employeeRepository, OutputWriter outputWriter) {
        this.employeeRepository = employeeRepository;
    }


    @GetMapping("/hello")
    public List<Employee> hello() {
        return employeeRepository.readEmployees();
    }

    @GetMapping("/getDummy")
    public Employee getDummy() {
        return new Employee(LocalDate.of(1990, 1, 1),
                20, 40, M, new Name("Test", "Dummy", "Unit"));

    };


}

