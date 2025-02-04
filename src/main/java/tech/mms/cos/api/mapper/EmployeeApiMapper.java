package tech.mms.cos.api.mapper;

import tech.mms.cos.api.model.NewEmployeeRequest;
import tech.mms.cos.core.model.Employee;

public class EmployeeApiMapper {

    public static Employee mapRequest(NewEmployeeRequest request) {

        return new Employee(
                request.getBirthdate(),
                request.getHourlyRate(),
                request.getHoursPerWeek(),
                request.getGender(),
                request.getName()
        );

    }

}
