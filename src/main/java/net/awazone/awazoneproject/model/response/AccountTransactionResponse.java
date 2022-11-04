package net.awazone.awazoneproject.model.response;

import lombok.Builder;
import lombok.Data;
import net.awazone.awazoneproject.model.aibopay.transaction.virtualTransactions.Pageable;
import net.awazone.awazoneproject.model.aibopay.transaction.virtualTransactions.Sort;
import net.awazone.awazoneproject.model.aibopay.transaction.virtualTransactions.Transaction;

@Data
@Builder
public class AccountTransactionResponse {
    private Transaction[] content;
    private Pageable pageable;
    private Integer totalElements;
    private Integer totalPages;
    private boolean last;
    private Sort sort;
    private boolean first;
    private Integer numberOfElements;
    private Integer size;
    private Integer number;
    private boolean empty;

}
