package net.awazone.awazoneproject.model.response;

import lombok.Data;

import java.util.Map;

@Data
public class ActivateBvnResponse {
    private String bvn;
    private Map<String, Object> name;
    private String dateOfBirth;
    private String mobileNo;

}
