package net.awazone.awazoneproject.model.requests.aibopay;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Builder
@Getter
@Setter
public class NewAccountRequest {

    private String accountType;
    private String customerUsername;
}
