package tech.mms.cos.io;

import tech.mms.cos.repository.Config;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class ApplicationConfigReader {

    private static String CONFIG_FILE_PATH = "config.ini";

    private static Config config = null;
    private static final Map<String, String> configMap = new HashMap<>();

    public static Config getConfig() {
        if (config != null) {
            return config;
        }

        read();
        String employeeJson = getEmployeeRepoFilename();
        String outputFilePath = getOutputFilePath();

        config = new Config(employeeJson, outputFilePath);
        return config;
    }


    private static void read() {
        configMap.clear();

        ClassLoader classLoader = ApplicationConfigReader.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(CONFIG_FILE_PATH);

        if (inputStream == null) {
            throw new RuntimeException("Config file was not found");
        }

        Scanner scanner = new Scanner(inputStream);

        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();

            String[] parts = line.split("=", 2);

            if (parts.length == 2) {
                configMap.put(parts[0], parts[1]);
            }

        }

        scanner.close();
    }

    public static String getEmployeeRepoFilename() {
        return configMap.get("employeeRepoFilename");
    }

    public static String getOutputFilePath() {
        return configMap.get("outputFilePath");
    }

    public static void setConfigFilePath(String configFilePath) {
        CONFIG_FILE_PATH = configFilePath;
        read();
    }

}

