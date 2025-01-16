package tech.mms.cos.repository;

import tech.mms.cos.model.Employee;

import java.util.List;

public interface EmployeeRepository {

    void saveEmployee(Employee employee);

    List<Employee> readEmployees();

}
