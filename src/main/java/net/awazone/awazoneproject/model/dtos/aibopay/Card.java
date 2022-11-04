package net.awazone.awazoneproject.model.dtos.aibopay;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Card {
    private String number;
    private String expiryMonth;
    private String expiryYear;
    private String pin;
    private String cvv;
}
