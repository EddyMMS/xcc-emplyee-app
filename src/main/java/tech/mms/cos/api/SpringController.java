package tech.mms.cos.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.mms.cos.api.client.EmployeesApi;
import tech.mms.cos.api.mapper.EmployeeApiMapper;
import tech.mms.cos.api.model.EmployeeDTO;
import tech.mms.cos.api.model.NewEmployeeRequestDTO;
import tech.mms.cos.core.RandomEmployeeService;
import tech.mms.cos.core.model.Employee;
import tech.mms.cos.core.model.Name;
import tech.mms.cos.repository.EmployeeRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static tech.mms.cos.core.model.Genders.M;

@RestController
public class SpringController implements EmployeesApi {

    private final EmployeeRepository employeeRepository;
    private final RandomEmployeeService employeeGeneratorApi;

    public SpringController(EmployeeRepository employeeRepository, RandomEmployeeService employeeGeneratorApi) {
        this.employeeRepository = employeeRepository;
        this.employeeGeneratorApi = employeeGeneratorApi;
    }


    @GetMapping("/getDummy")
    public Employee getDummy() {
        var employee = new Employee(LocalDate.of(1990, 1, 1),
                20, 40, M, new Name("Test", "Dummy", "Unit"), "HR");
        employeeRepository.saveEmployee(employee);
        return employee;

    }

    @GetMapping("/random")
    public Employee getRandomEmployee() {

        // 1. Erstelle neue Klasse RandomEmployeeGeneratorApi
        // 2. Rufe hier RandomEmployeeGeneratorApi auf, return type ist Employee
        // 3. Spring Boot how to call rest api (RestTemplate)  (Nicht reactive! Nicht WebClient)
        // 4. Schau dir die Response der API an, baue eine Klasse (Mehrere Klassen) die der Response entsprechen.
        // https://randomuser.me/api/

        return employeeGeneratorApi.generate();
    }


    @Override
    public ResponseEntity<EmployeeDTO> employeesPost(NewEmployeeRequestDTO employeeDTO) {
        Employee employee = employeeRepository.saveEmployee(EmployeeApiMapper.mapRequest(employeeDTO));
        return ResponseEntity.ok(EmployeeApiMapper.mapResponse(employee));
    }

    @Override
    public ResponseEntity<Void> employeesUuidDelete(String uuid) {
        var wasDeleted = employeeRepository.deleteEmployee(UUID.fromString(uuid));

        if (wasDeleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<EmployeeDTO> employeesUuidGet(String uuid) {

        try {
            UUID id = UUID.fromString(uuid);

            return employeeRepository.find(id)
                    .map(EmployeeApiMapper::mapResponse)
                    .map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Override
    public ResponseEntity<EmployeeDTO> employeesUuidPut(String uuid, EmployeeDTO employeeDTO) {
        return null; //TODO
    }

    @Override
    public ResponseEntity<List<EmployeeDTO>> listAllEmployees() {
        var employees = employeeRepository.readEmployees();
        return ResponseEntity.ok(employees.stream()
                .map(EmployeeApiMapper::mapResponse)
                .toList()
        );
    }


}

