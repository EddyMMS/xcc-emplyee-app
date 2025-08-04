package tech.mms.cos.config;

import static tech.mms.cos.config.ApiConfig.createBasicObjectMapper;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class AdapterConfiguration {

  @Bean
  public RestTemplate restTemplate(
      @Qualifier("externalApiObjectMapper") ObjectMapper objectMapper) {
    RestTemplate restTemplate = new RestTemplate();
    restTemplate
        .getMessageConverters()
        .add(0, createMappingJacksonHttpMessageConverter(objectMapper));

    return restTemplate;
  }

  @Bean
  public ObjectMapper externalApiObjectMapper() {
    return createBasicObjectMapper()
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
  }

  private MappingJackson2HttpMessageConverter createMappingJacksonHttpMessageConverter(
      ObjectMapper objectMapper) {
    MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
    converter.setObjectMapper(objectMapper);
    return converter;
  }
}
