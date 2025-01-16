package tech.mms.cos.repository;

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
