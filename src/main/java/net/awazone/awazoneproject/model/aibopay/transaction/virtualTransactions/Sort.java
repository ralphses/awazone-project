package net.awazone.awazoneproject.model.aibopay.transaction.virtualTransactions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Sort {
    private boolean sort;
    private boolean unsorted;
    private boolean empty;
}
