/*
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

 */
