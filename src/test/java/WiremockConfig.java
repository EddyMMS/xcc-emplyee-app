import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import tech.mms.cos.adapter.random.model.RandomEmployeeEmployeeDTO;
import tech.mms.cos.adapter.random.model.RandomEmployeeNameDTO;
import tech.mms.cos.adapter.random.model.RandomEmployeeResultsDTO;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;

@Component
public class WiremockConfig {

    @Autowired
    ObjectMapper objectMapper;

    void createBasicStubs() {
        try {
            stubRandomEmployee();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    void stubRandomEmployee() throws JsonProcessingException {
        var responseBody = new RandomEmployeeResultsDTO()
                .results(
                        List.of(new RandomEmployeeEmployeeDTO()
                                .gender("male")
                                .name(
                                        new RandomEmployeeNameDTO()
                                                .first("Hans")
                                                .last("Tolonen")
                                )
                        )
                );

        stubRandomEmployee(responseBody);
    }

    void stubRandomEmployee(RandomEmployeeResultsDTO randomEmployeeResultsDTO) throws JsonProcessingException {
        stubFor(
                get("/random-employee/api/")
                        .willReturn(ok(objectMapper.writeValueAsString(randomEmployeeResultsDTO))
                                .withHeader("content-type", "application/json"))
        );
    }

}
