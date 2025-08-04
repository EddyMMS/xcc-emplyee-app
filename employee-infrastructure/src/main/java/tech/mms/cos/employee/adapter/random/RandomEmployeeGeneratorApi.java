package tech.mms.cos.employee.adapter.random;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tech.mms.cos.adapter.random.ApiClient;
import tech.mms.cos.adapter.random.client.RandomEmployeeApi;
import tech.mms.cos.adapter.random.model.RandomEmployeeEmployeeDTO;
import tech.mms.cos.adapter.random.model.RandomEmployeeNameDTO;
import tech.mms.cos.core.employee.RandomEmployeeService;
import tech.mms.cos.core.employee.model.Employee;
import tech.mms.cos.core.employee.model.Genders;
import tech.mms.cos.core.employee.model.Name;

@Service
public class RandomEmployeeGeneratorApi implements RandomEmployeeService {

  private final RandomEmployeeApi apiClient;

  public RandomEmployeeGeneratorApi(
      RestTemplate restTemplate, @Value("${adapter.randomEmployee.baseUrl:null}") String baseUrl) {
    var baseApiClient = new ApiClient(restTemplate);
    if (baseUrl != null && !baseUrl.equalsIgnoreCase("null")) {
      baseApiClient.setBasePath(baseUrl);
    }
    this.apiClient = new RandomEmployeeApi(baseApiClient);
  }

  @Override
  public Employee generate() {

    RandomEmployeeEmployeeDTO employee = getRandomEmployee();

    return new Employee(
        this.getRandomDate(),
        this.getRandomHourlyRate(),
        this.getRandomHoursPerWeek(),
        this.parseGender(employee.getGender()),
        this.parseName(employee.getName()),
        this.generateDepartment());
  }

  private String generateDepartment() {
    return "HR";
  }

  private Name parseName(RandomEmployeeNameDTO name) {
    return new Name(name.getFirst(), null, name.getLast());
  }

  private Genders parseGender(String gender) {
    return switch (gender) {
      case "female" -> Genders.W;
      case "male" -> Genders.M;
      default -> Genders.D;
    };
  }

  private RandomEmployeeEmployeeDTO getRandomEmployee() {
    List<RandomEmployeeEmployeeDTO> randomEmployees = apiClient.getRandomEmployees().getResults();

    if (randomEmployees.isEmpty()) {
      throw new IllegalStateException("Random api returned empty list");
    }

    return randomEmployees.get(0);
  }

  public LocalDate getRandomDate() {
    return RandomDateGenerator.generateRandomDate(1970, 2000);
  }

  public double getRandomHourlyRate() {
    return RandomSalaryGenerator.randomHourlyRate();
  }

  public int getRandomHoursPerWeek() {
    return RandomSalaryGenerator.randomHoursPerWeek();
  }
}
