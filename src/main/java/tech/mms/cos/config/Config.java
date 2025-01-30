package tech.mms.cos.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Config {

    private final String employeeRepoFilename;

    private final String outputFilePath;

    public Config(String employeeRepoFilename, String outputFilePath) {
        this.employeeRepoFilename = employeeRepoFilename;
        this.outputFilePath = outputFilePath;
    }

    public String getEmployeeRepoFilename() {
        return employeeRepoFilename;
    }

    public String getOutputFilePath() {
        return outputFilePath;
    }


}
