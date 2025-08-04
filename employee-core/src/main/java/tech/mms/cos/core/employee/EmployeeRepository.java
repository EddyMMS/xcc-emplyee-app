package tech.mms.cos.core.employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import tech.mms.cos.core.employee.model.Employee;

public interface EmployeeRepository {

  Employee saveEmployee(Employee employee);

  List<Employee> readEmployees();

  Optional<Employee> find(UUID uuid);

  boolean deleteEmployee(UUID uuid);

  void clear();
}
