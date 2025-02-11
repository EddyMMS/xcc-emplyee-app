package tech.mms.cos.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.zalando.problem.jackson.ProblemModule;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Configuration
public class SpringApplicationConfig {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return createBasicObjectMapper();
    }

    public static ObjectMapper createBasicObjectMapper() {
        return new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
            .registerModule(new ProblemModule().withStackTraces(false));
    }

    @Bean
    public Config config(
        @Value("${app.outputFilePath}") String outputFilePath,
        @Value("${app.employeeRepoFilename}") String employeeRepoFilename
    ) {
        return new Config(employeeRepoFilename, outputFilePath);
    }


}
