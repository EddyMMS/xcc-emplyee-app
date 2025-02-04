package tech.mms.cos.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import tech.mms.cos.config.Config;
import tech.mms.cos.core.model.Employee;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class FileEmployeeRepository implements EmployeeRepository {

    private final ObjectMapper objectMapper;

    private final File file;

    public FileEmployeeRepository(Config config, ObjectMapper objectMapper) {
        file = new File(config.getEmployeeRepoFilename());
        this.objectMapper = objectMapper;
    }

    @Override
    public Employee saveEmployee(Employee employee) {

        List<Employee> employeeList = readEmployees();

        employeeList.add(employee);

        try {
            objectMapper.writeValue(file, employeeList);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return employee;

    }

    @Override
    public List<Employee> readEmployees() {

        List<Employee> employees = new ArrayList<>();

        if (file.exists()) {
            try {
                employees = objectMapper.readValue(file, new TypeReference<List<Employee>>() {
                });
                // System.out.println("The JSON-File was successfully made");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return employees;
    }

    @Override
    public Optional<Employee> find(UUID uuid) {
        return readEmployees().stream().filter(employee -> employee.getUuid().equals(uuid.toString())).findFirst();
    }

    @Override
    public boolean deleteEmployee(UUID uuid) {
        return false;
    }


}