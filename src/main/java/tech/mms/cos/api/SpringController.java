package tech.mms.cos.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.mms.cos.api.mapper.EmployeeApiMapper;
import tech.mms.cos.api.model.NewEmployeeRequest;
import tech.mms.cos.core.RandomEmployeeService;
import tech.mms.cos.core.model.Employee;
import tech.mms.cos.core.model.Name;
import tech.mms.cos.repository.EmployeeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static tech.mms.cos.core.model.Genders.M;

@RestController
public class SpringController {

    private final EmployeeRepository employeeRepository;
    private final RandomEmployeeService employeeGeneratorApi;

    public SpringController(EmployeeRepository employeeRepository, RandomEmployeeService employeeGeneratorApi) {
        this.employeeRepository = employeeRepository;
        this.employeeGeneratorApi = employeeGeneratorApi;
    }


    @GetMapping("/getDummy")
    public Employee getDummy() {
        var employee = new Employee(LocalDate.of(2025, 1, 1),
                20, 40, M, new Name("Test", "Dummy", "Unit"));
        employeeRepository.saveEmployee(employee);
        return employee;

    };

    @GetMapping("/random")
    public Employee getRandomEmployee() {

        // 1. Erstelle neue Klasse RandomEmployeeGeneratorApi
        // 2. Rufe hier RandomEmployeeGeneratorApi auf, return type ist Employee
        // 3. Spring Boot how to call rest api (RestTemplate)  (Nicht reactive! Nicht WebClient)
        // 4. Schau dir die Response der API an, baue eine Klasse (Mehrere Klassen) die der Response entsprechen.
        // https://randomuser.me/api/

        return employeeGeneratorApi.generate();
    }

    @GetMapping("/employees")
    public List<Employee> getEmployees(){
        return employeeRepository.readEmployees();
    };

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployeeByUUID(@PathVariable String id) {

        try {
            UUID uuid = UUID.fromString(id);

            return employeeRepository.find(uuid)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/employees/{uuid}")
    public ResponseEntity<Void> deleteEmployeeByUUID(@PathVariable UUID uuid){
        var wasDeleted = employeeRepository.deleteEmployee(uuid);

        if (wasDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }

    };

    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestBody NewEmployeeRequest newEmployee){
        Employee employee = employeeRepository.saveEmployee(EmployeeApiMapper.mapRequest(newEmployee));
        return ResponseEntity.ok(employee);
    };


}

