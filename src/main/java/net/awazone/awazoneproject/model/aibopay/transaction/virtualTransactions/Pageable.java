package net.awazone.awazoneproject.model.aibopay.transaction.virtualTransactions;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Pageable {
    private Sort sort;
    private String pageSize;
    private String pageNumber;
    private String offset;
    private boolean unpaged;
    private boolean paged;

}
