package net.awazone.awazoneproject.model.dtos.aibopay.monnify.webhook;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AccountTransactionNotificationRequest {
    private String eventType;
    private EventData eventData;

}
