package tech.mms.cos.api.mapper;


import tech.mms.cos.api.model.EmployeeDTO;
import tech.mms.cos.api.model.NameDTO;
import tech.mms.cos.api.model.NewEmployeeRequestDTO;
import tech.mms.cos.core.model.Employee;
import tech.mms.cos.core.model.Genders;
import tech.mms.cos.core.model.Name;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class EmployeeApiMapper {

    public static Employee mapRequest(NewEmployeeRequestDTO request) {

        return new Employee(
                LocalDate.parse(request.getBirthdate()),
                request.getHourlyRate(),
                request.getHoursPerWeek(),
                Genders.valueOf(request.getGender().name()),
                new Name(request.getName().getFirstName(),
                        request.getName().getMiddleName(),
                        request.getName().getLastName()
                )
        );

    }

    public static EmployeeDTO mapResponse(Employee response) {
        return new EmployeeDTO()
                .uuid(response.getUuid())
                .birthdate(response.getBirthdate().format(DateTimeFormatter.ISO_DATE))
                .hourlyRate(response.getHourlyRate())
                .hoursPerWeek(response.getHoursPerWeek())
                .gender(EmployeeDTO.GenderEnum.valueOf(response.getGender().name()))
                .name(
                        new NameDTO()
                                .firstName(response.getName().getFirstName())
                                .middleName(response.getName().getMiddleName())
                                .lastName(response.getName().getLastName())
                );
    }

}
