import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestClient;
import tech.mms.cos.adapter.random.RandomEmployeeGeneratorApi;
import tech.mms.cos.adapter.random.model.RandomEmployeeEmployeeDTO;
import tech.mms.cos.adapter.random.model.RandomEmployeeNameDTO;
import tech.mms.cos.adapter.random.model.RandomEmployeeResultsDTO;
import tech.mms.cos.core.model.Employee;
import tech.mms.cos.core.model.Genders;
import tech.mms.cos.core.model.Name;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

public class WireMockTestIT extends AbstractIntegrationTest {

    @Value("${wiremock.server.baseUrl}")
    private String wireMockUrl;

    @Test
    void returns_a_ping() {
        stubFor(get("/ping").willReturn(ok("test")));

        RestClient client = RestClient.create();
        String body = client.get()
                .uri(wireMockUrl + "/ping")
                .retrieve()
                .body(String.class);

        Assertions.assertEquals("test", body);

    }

    @Autowired
    RandomEmployeeGeneratorApi employeeGeneratorApi;

    @Autowired
    ObjectMapper objectMapper;

    @Test
    void generateTest() throws JsonProcessingException {

        var responseBody = new RandomEmployeeResultsDTO()
                .results(
                        List.of(new RandomEmployeeEmployeeDTO()
                                .gender("female")
                                .name(
                                        new RandomEmployeeNameDTO()
                                                .first("Hans")
                                                .last("Musterfrau")
                                )
                        )
                );

        wiremockConfig.stubRandomEmployee(responseBody);

        Employee expectedEmployee = employeeGeneratorApi.generate();

        Assertions.assertEquals(expectedEmployee.getGender(), Genders.W);
        Assertions.assertEquals(expectedEmployee.getName(), new Name("Hans", null, "Musterfrau"));

    }


    /*
    TODO: generate() testen
    RandomEmyployeeDTO
    -> Welchen Statuscode würde man hier am besten verwenden?
    201 Created

    Error Codes
    Difference 200/201
    Wann macht welcher Statuscode sinn?

    200 OK: Die Anfrage war erfolgreich und die Antwort enthält die angeforderten Informationen.
    201 Created: Die Anfrage war erfolgreich und eine neue Ressource wurde erstellt.

    400 Bad Request: Die Anfrage war ungültig oder konnte vom Server nicht verarbeitet werden.
    401 Unauthorized: Authentifizierung ist erforderlich und wurde entweder nicht bereitgestellt oder war fehlerhaft.
    403 Forbidden: Der Server versteht die Anfrage, verweigert aber den Zugriff.
    404 Not Found: Die angeforderte Ressource konnte auf dem Server nicht gefunden werden.
    500 Internal Server Error: Ein allgemeiner Fehler trat auf dem Server auf.

     */
}
