package tech.mms.cos.adapter.random.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import tech.mms.cos.adapter.random.RandomEmployeeGeneratorApi;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RandomEmployeeResultList {
    public List<RandomEmployee> results;
}
