package tech.mms.cos.adapter.random.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class RandomEmployeeName {
    public String first;
    public String last;
}