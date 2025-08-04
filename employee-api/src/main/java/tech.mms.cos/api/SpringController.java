package tech.mms.cos.api;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.mms.cos.api.client.EmployeesApi;
import tech.mms.cos.api.mapper.EmployeeApiMapper;
import tech.mms.cos.api.model.EmployeeDTO;
import tech.mms.cos.api.model.NewEmployeeRequestDTO;
import tech.mms.cos.auth.AuthenticationService;
import tech.mms.cos.auth.model.AuthenticatedAccount;
import tech.mms.cos.core.auth.account.model.AccountRole;
import tech.mms.cos.core.employee.EmployeeRepository;
import tech.mms.cos.core.employee.RandomEmployeeService;
import tech.mms.cos.core.employee.model.Employee;
import tech.mms.cos.core.employee.model.Genders;
import tech.mms.cos.core.employee.model.Name;
import tech.mms.cos.exception.AppValidationException;

@RestController
@RequestMapping("/")
public class SpringController implements EmployeesApi {

  private final EmployeeRepository employeeRepository;
  private final RandomEmployeeService employeeGeneratorApi;

  public SpringController(
      EmployeeRepository employeeRepository, RandomEmployeeService employeeGeneratorApi) {
    this.employeeRepository = employeeRepository;
    this.employeeGeneratorApi = employeeGeneratorApi;
  }

  @GetMapping("/getDummy")
  @PreAuthorize("hasAnyRole('EDITOR', 'ADMIN')")
  public Employee getDummy() {
    var employee =
        new Employee(
            LocalDate.of(1990, 1, 1), 20, 40, Genders.M, new Name("Test", "Dummy", "Unit"), "HR");
    employeeRepository.saveEmployee(employee);
    return employee;
  }

  @GetMapping("/random")
  public Employee getRandomEmployee() {

    var randomEmployee = employeeGeneratorApi.generate();
    employeeRepository.saveEmployee(randomEmployee);
    return randomEmployee;
  }

  @Override
  public ResponseEntity<EmployeeDTO> employeesPost(NewEmployeeRequestDTO newEmployeeRequestDTO) {
    Employee employee =
        employeeRepository.saveEmployee(EmployeeApiMapper.mapRequest(newEmployeeRequestDTO));
    return ResponseEntity.ok(EmployeeApiMapper.mapResponse(employee));
  }

  @Override
  public ResponseEntity<Void> employeesUuidDelete(String uuid) {

    /*

    3 Rollen für Accounts: Viewer, Editor, Admin
    Sinnvolle authorization für die jeweiligen Eployee Endpoints, z.B. getAll -> Viewer&Editor, delete -> Editor, Admin -> Alles

    Wenn Rolle < ADMIN, dann braucht Account für Löschen das gleiche Department wie Employee

     */

    var employee = employeeRepository.find(UUID.fromString(uuid)).orElse(null);

    if (employee == null) {
      return ResponseEntity.notFound().build();
    }

    // Was ist OAuth 2.0, was ist ein JWT Token, Aufbau JWT Token, Was sind OAuth Flows,
    // Authorization code flow (Video)
    // Video: How to use OAtuh with Github (Anlegen einer Github OAuth App)

    /*

    ##-- OAuth Flows --##
    1. Baue im UI einen Login Button, welcher dich direkt auf Github weiterleitet zum Authorisieren
    Client ID -> Kann ich öffentlich machen
    Client Secret -> Nur im Backend als Secret, (nicht öffentlich machen)


    2. Bauene eine Page auf der Redirect Url,
     welche den "code" einließt und zum Backend schickt auf {backendUrl}/authlogin/oauth/github/access_token

    3. Erstellen im Backend einen AuthController,
     welcher auf /auth/login/oauth/github/access_token hört,
      den "code" entgegennimmt, und den Code für ein OAuth Token austauscht

    4. Das Frontend kriegt den OAuth Token vom Backened
    als antwort zurück und speichert das OAuth Token im Redux Store


    https://github.com/settings/applications/3045564
    https://docs.github.com/de/apps/oauth-apps/building-oauth-apps/authorizing-oauth-apps
    https://docs.github.com/de/apps/oauth-apps/building-oauth-apps/scopes-for-oauth-apps


     */

    AuthenticatedAccount auth = AuthenticationService.getAccountAuth();

    if (!auth.hasHigherPermissionThan(AccountRole.EDITOR)) {
      return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }

    if (auth.isBetween(AccountRole.EDITOR, AccountRole.ADMIN)) {
      var userDepartments = auth.getDetails().getDepartments();
      if (!userDepartments.contains(employee.getDepartment())) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
      }
    }

    var wasDeleted = employeeRepository.deleteEmployee(UUID.fromString(uuid));

    SecurityContextHolder.getContext().getAuthentication();

    if (wasDeleted) {
      return ResponseEntity.noContent().build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @Override
  public ResponseEntity<EmployeeDTO> employeesUuidGet(String uuid) {

    try {
      UUID id = UUID.fromString(uuid);

      return employeeRepository
          .find(id)
          .map(EmployeeApiMapper::mapResponse)
          .map(ResponseEntity::ok)
          .orElse(ResponseEntity.notFound().build());

    } catch (IllegalArgumentException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @Override
  public ResponseEntity<EmployeeDTO> employeesUuidPut(String uuid, EmployeeDTO employeeDTO) {
    try {
      UUID id = UUID.fromString(uuid);

      Optional<Employee> existingEmployeeOpt = employeeRepository.find(id);

      if (existingEmployeeOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
      }

      Employee existingEmployee = existingEmployeeOpt.get();

      LocalDate birthdate = LocalDate.parse(employeeDTO.getBirthdate());

      existingEmployee.setBirthdate(birthdate).setHourlyRate(employeeDTO.getHourlyRate());
      existingEmployee.setHoursPerWeek(employeeDTO.getHoursPerWeek());
      existingEmployee.setGender(Genders.valueOf(employeeDTO.getGender().name()));
      existingEmployee.setName(
          new Name(
              employeeDTO.getName().getFirstName(),
              employeeDTO.getName().getLastName(),
              employeeDTO.getName().getMiddleName()));
      existingEmployee.setDepartment(employeeDTO.getDepartment());

      existingEmployee.validate();

      Employee result = employeeRepository.saveEmployee(existingEmployee);

      return ResponseEntity.ok(EmployeeApiMapper.mapResponse(result));

    } catch (IllegalArgumentException | AppValidationException e) {
      return ResponseEntity.badRequest().build();
    }
  }

  @Override
  public ResponseEntity<List<EmployeeDTO>> listAllEmployees() {
    var employees = employeeRepository.readEmployees();
    return ResponseEntity.ok(employees.stream().map(EmployeeApiMapper::mapResponse).toList());
  }
}
