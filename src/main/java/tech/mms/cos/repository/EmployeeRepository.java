package tech.mms.cos.repository;

import java.util.List;

import tech.mms.cos.model.Employee;

public interface EmployeeRepository {

    void saveEmployee(Employee employee);

    List<Employee> readEmployees();

}
