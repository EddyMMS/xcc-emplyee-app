package tech.mms.cos;

import tech.mms.cos.io.ApplicationConfigReader;
import tech.mms.cos.io.CombinedOutputWriter;
import tech.mms.cos.io.ConsoleOutputWriter;
import tech.mms.cos.io.FileOutputWriter;
import tech.mms.cos.repository.FileEmployeeRepository;

import java.util.HashMap;
import java.util.Map;

public class ServiceLocator {

    private static ServiceLocator serviceLocator;
    private final Map<String, Object> objects = new HashMap<>();

    private ServiceLocator() {
        this.put("ApplicationConfig", ApplicationConfigReader.getConfig());

        this.put("EmployeeRepository", new FileEmployeeRepository(this.get("ApplicationConfig")));

        this.put("ConsoleOutputWriter", new ConsoleOutputWriter());
        this.put("FileOutputWriter", new FileOutputWriter(this.get("ApplicationConfig")));
        this.put("CombinedOutputWriter", new CombinedOutputWriter(this.get("FileOutputWriter"), this.get("ConsoleOutputWriter")));

        this.put("OutputWriter", this.get("CombinedOutputWriter"));
    }

    public static ServiceLocator getInstance() {
        if (serviceLocator == null) {
            serviceLocator = new ServiceLocator();
        }

        return serviceLocator;
    }

    public void put(String key, Object value) {
        objects.put(key, value);
    }

    public <T> T get(String key) {
        return (T) objects.get(key);
    }


}