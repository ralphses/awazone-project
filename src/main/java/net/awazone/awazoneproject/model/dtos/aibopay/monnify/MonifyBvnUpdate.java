package net.awazone.awazoneproject.model.dtos.aibopay.monnify;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonifyBvnUpdate {
    private String bvn;
}
