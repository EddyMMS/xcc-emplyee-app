package tech.mms.cos;

import java.util.List;

public interface EmployeeRepository {

    void saveEmployee(Employee employee);

    List<Employee> readEmployees();

}
