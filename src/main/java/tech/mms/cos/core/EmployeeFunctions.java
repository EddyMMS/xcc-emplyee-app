package tech.mms.cos.core;

import org.springframework.stereotype.Component;
import tech.mms.cos.core.model.Employee;
import tech.mms.cos.core.model.Genders;
import tech.mms.cos.repository.EmployeeRepository;

import java.util.Comparator;
import java.util.List;

@Component
public class EmployeeFunctions {

    private final EmployeeRepository employeeRepository;

    public EmployeeFunctions(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
// TODO: integration test für EmployeeFunctions
    // TODO: getTopEarningEmployees für n = 1; (mongodb laufen lassen über flapdoodle)

    public List<Employee> getTopEarningEmployees(int n) {
        List<Employee> employeeList = employeeRepository.readEmployees();

        employeeList.sort(Comparator.comparingDouble(Employee::getMonthlySalary).reversed());

        if (n >= 0) {
            return employeeList.subList(0, Math.min(n, employeeList.size()));
        } else {
            return employeeList.subList(0, 0);
        }

    }

    public double getAverageAge() {

        // Gleiche Funktion bloß ohne Streams
        List<Employee> employeeList = employeeRepository.readEmployees();
        return getAverageAge(employeeList);
    }

    public double getAverageAge(List<Employee> employeeList) {

        int totalAge = 0;
        int count = 0;

        for (Employee employee : employeeList) {
            totalAge += employee.getAge();
            count++;
        }

        return count > 0 ? (double) totalAge / count : 0.0;

    }


    // Ohne Streams / Collector
    // Gender als Übergabeparameter

    public double avgSalByGen(Genders gender) {
        List<Employee> employeeList = employeeRepository.readEmployees();

        double totalSalary = 0;
        int counter = 0;

        for (Employee employee : employeeList) {
            if (gender == employee.getGender()) {
                totalSalary += employee.getMonthlySalary();
                counter++;
            }
        }

        return counter > 0 ? totalSalary / counter : 0.0;
    }

    public double avgSalbyAge(int x) {

        List<Employee> employeeList = employeeRepository.readEmployees();

        double totalSalary = 0;
        int counter = 0;

        for (Employee employee : employeeList) {
            if (employee.getAge() == x) {
                totalSalary += employee.getMonthlySalary();
                counter++;
            }
        }

        return totalSalary / counter;

    }

    public Employee topEarningsByGender(Genders genders) {

        List<Employee> employeeList = employeeRepository.readEmployees();

        double topEarning = 0;
        Employee topEarningEmployee = null;

        for (Employee employee : employeeList) {
            if (genders == employee.getGender() && topEarning < employee.getMonthlySalary()) {
                topEarning = employee.getMonthlySalary();
                topEarningEmployee = employee;
            }
        }

        return topEarningEmployee;
    }


    // Durchschnittsalter aller Mitarbeiter
    // Input Alter -> Durchschnittsverdienst aller Mitarbeiter mit dem Alter

    // Durchschnittsverdienst pro Geschlecht
    // Topverdiener pro Geschlecht
    // Niedrigverdiener pro Schlecht


    //public void topVerdiener(Gender g)
    //    return test(g, false)

    //private void test(Gender g, boolean reversed)

}
