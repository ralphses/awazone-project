package net.awazone.awazoneproject.model.dtos.aibopay.coinremmitter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CoinRemmitterResponse {
    private int flag;
    private String msg;
    private String action;
    private Map<String, Object> data;
}
