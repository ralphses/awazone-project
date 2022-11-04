package net.awazone.awazoneproject.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Bank {
    private String name;
    private String code;
    private String ussdTemplate;
    private String baseUssdCode;
    private String transferUssdTemplate;
}
