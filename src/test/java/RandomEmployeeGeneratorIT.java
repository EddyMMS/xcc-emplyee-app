import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;
import tech.mms.cos.adapter.random.RandomEmployeeGeneratorApi;
import tech.mms.cos.adapter.random.client.RandomEmployeeApi;
import tech.mms.cos.adapter.random.model.RandomEmployeeEmployeeDTO;
import tech.mms.cos.core.model.Employee;

import java.time.LocalDate;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.bson.assertions.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.data.mongodb.core.aggregation.ConditionalOperators.Switch.CaseOperator.when;


public class RandomEmployeeGeneratorIT extends AbstractIntegrationTest {

    private WireMockServer wireMockServer;
    private RandomEmployeeGeneratorApi randomEmployeeGeneratorApi;

    @BeforeEach
    void setUp() {

        wireMockServer = new WireMockServer(4000);
        wireMockServer.start();

        String baseUrl = "http://localhost:4000/random-employee/api";

        RestTemplate restTemplate = new RestTemplate();
        randomEmployeeGeneratorApi = new RandomEmployeeGeneratorApi(restTemplate, baseUrl);
    }

    @AfterEach
    void tearDown() {
        wireMockServer.stop();
    }

    @Test
    void testGenerate() {

    }
}