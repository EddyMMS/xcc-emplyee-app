package tech.mms.cos.repository;

import tech.mms.cos.core.model.Employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository {

    Employee saveEmployee(Employee employee);

    List<Employee> readEmployees();

    Optional<Employee> find(UUID uuid);

    boolean deleteEmployee(UUID uuid);

    void clear();
}
