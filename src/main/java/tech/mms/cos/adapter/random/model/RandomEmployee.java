package tech.mms.cos.adapter.random.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tech.mms.cos.adapter.random.RandomEmployeeGeneratorApi;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RandomEmployee {
    public String gender;
    public RandomEmployeeName name;
}
