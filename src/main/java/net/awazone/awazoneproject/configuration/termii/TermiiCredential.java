package net.awazone.awazoneproject.configuration.termii;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "termii")
public class TermiiCredential {
    private String api_key;
    private String type;
    private String channel;
}
