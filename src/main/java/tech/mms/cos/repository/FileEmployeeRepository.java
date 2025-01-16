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

        // Gib mir alle Employees in einer Liste. Füg den neuen Employee der Liste hinzu. Speicher Liste ab.

        List<Employee> employeeList = readEmployees();

        employeeList.add(employee);

        try {
            objectMapper.writeValue(file, employeeList);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    // Anfangs, frag den User, ob er neuen Employee anlegen will, oder alle Employees ausgeben

    // Array List unterschied nachlesen

    @Override
    public List<Employee> readEmployees() {

        // Gib List zurück. Im Fehlerfall, leere Liste zurückgeben. Überprüfe, ob employee.json existiert, wenn nicht gib leere Liste zurück.
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

/*
Speichern des Users in eine List<Employee> in einem Datei "employees.json"
Neuer User -> Auslesen der Datei als List<Employee> -> Add new employee to List -> Save List to file again
ObjectMapper Jackson -> Json De-/Serializer -> Object => String -> String => Object
 */
