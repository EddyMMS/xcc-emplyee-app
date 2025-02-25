import tech.mms.cos.core.model.Employee;
import tech.mms.cos.core.model.Genders;
import tech.mms.cos.core.model.Name;

import java.time.LocalDate;

public class TestEmployeeProvider {

    static class EmployeeBuilder {

        private Genders gender = Genders.M;
        private double hourlyRate = 25;
        private int hoursPerWeek = 40;
        private LocalDate birthdate = LocalDate.of(1990, 1, 1);

        public EmployeeBuilder gender(Genders genders) {
            this.gender = genders;
            return this;
        }

        public EmployeeBuilder hourlyRate(double hourlyRate) {
            this.hourlyRate = hourlyRate;
            return this;
        }

        public EmployeeBuilder hoursPerWeek(int hoursPerWeek) {
            this.hoursPerWeek = hoursPerWeek;
            return this;
        }

        public EmployeeBuilder birthdate(LocalDate birthdate){
            this.birthdate = birthdate;
            return this;
        }

        public EmployeeBuilder monthlySalary(double monthlySalary) {
            this.hoursPerWeek = 40;
            this.hourlyRate = monthlySalary / 4 / this.hourlyRate;
            return this;
        }

        public Employee build() {
            return new Employee(
                    this.birthdate,
                    this.hourlyRate,
                    this.hoursPerWeek,
                    this.gender,
                    new Name("John", "Bob", "Man")
            );
        }

    }

}
