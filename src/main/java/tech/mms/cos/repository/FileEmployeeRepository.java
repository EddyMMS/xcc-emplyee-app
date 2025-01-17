package tech.mms.cos.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import tech.mms.cos.model.Employee;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileEmployeeRepository implements EmployeeRepository {

    File file;
    ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    public FileEmployeeRepository(Config config) {
        file = new File(config.getEmployeeRepoFilename());
    }

    @Override
    public void saveEmployee(Employee employee) {

        List<Employee> employeeList = readEmployees();

        employeeList.add(employee);

        try {
            objectMapper.writeValue(file, employeeList);
        } catch (IOException e) {
            e.printStackTrace();
        }


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
}