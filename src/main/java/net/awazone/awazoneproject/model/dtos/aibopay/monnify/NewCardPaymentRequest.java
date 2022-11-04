package net.awazone.awazoneproject.model.dtos.aibopay.monnify;

import lombok.Builder;
import lombok.Data;
import net.awazone.awazoneproject.model.dtos.aibopay.Card;

@Data
@Builder
public class NewCardPaymentRequest {
    private String transactionReference;
    private String collectionChannel;
    private Card card;
}
