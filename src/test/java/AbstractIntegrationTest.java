import com.github.tomakehurst.wiremock.client.WireMock;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;
import tech.mms.cos.SpringBootApp;
import tech.mms.cos.repository.EmployeeRepository;

@SpringBootTest(classes = {SpringBootApp.class, WiremockConfig.class})
@ActiveProfiles("integration-test")
@AutoConfigureDataMongo
@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "de.flapdoodle.mongodb.embedded.version=5.0.20"
})
@EnableWireMock(
        @ConfigureWireMock(port = 10400)
)
@Tag("IntegrationTest")
public abstract class AbstractIntegrationTest {


    @Autowired
    public EmployeeRepository employeeRepository;

    @Autowired
    public WiremockConfig wiremockConfig;

    @BeforeEach
    void initTest() {
        employeeRepository.clear();
        wiremockConfig.createBasicStubs();
    }

    @AfterEach
    void tearDown() {
        employeeRepository.clear();
        WireMock.removeAllMappings();
    }

}
