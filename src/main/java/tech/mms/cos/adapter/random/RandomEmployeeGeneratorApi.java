package tech.mms.cos.adapter.random;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tech.mms.cos.adapter.random.model.RandomEmployee;
import tech.mms.cos.adapter.random.model.RandomEmployeeResultList;
import tech.mms.cos.core.RandomEmployeeService;
import tech.mms.cos.core.model.Employee;
import tech.mms.cos.core.model.Genders;
import tech.mms.cos.core.model.Name;

import java.time.LocalDate;

/**
 * https://editor.swagger.io/ <- Spielwiese
 * Swagger / OpenApi
 * mvn open api generator plugin
 *
 */

@Service
public class RandomEmployeeGeneratorApi implements RandomEmployeeService {

    private static final String url = "https://randomuser.me/api/";

    private final RestTemplate restTemplate;

    public RandomEmployeeGeneratorApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public Employee generate() {

        RandomEmployee employee = getRandomEmployee();

        return new Employee(
                this.getRandomDate(),
                this.getRandomHourlyRate(),
                this.getRandomHoursPerWeek(),
                this.parseGender(employee.gender),
                this.parseName(employee)
        );
    }

    private Name parseName(RandomEmployee employee) {
        return new Name(
                employee.name.first,
                null,
                employee.name.last
        );
    }

    private Genders parseGender(String gender) {
        return switch (gender) {
            case "female" -> Genders.W;
            case "male" -> Genders.M;
            default -> Genders.D;
        };
    }

    private RandomEmployee getRandomEmployee() {
        ResponseEntity<RandomEmployeeResultList> response = restTemplate.getForEntity(url, RandomEmployeeResultList.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new IllegalStateException("Error while calling random user api.");
        }

        RandomEmployeeResultList body = response.getBody();

        if (body == null || body.results.isEmpty()) {
            throw new IllegalStateException("Random api returned null body");
        }

        return body.results.get(0);
    }

    public LocalDate getRandomDate() {
        return RandomDateGenerator.generateRandomDate(1970, 2000);
    }

    public double getRandomHourlyRate(){
       return RandomSalaryGenerator.randomHourlyRate();

    }

    public int getRandomHoursPerWeek(){
        return RandomSalaryGenerator.randomHoursPerWeek();
    }

}