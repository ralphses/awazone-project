package net.awazone.awazoneproject.configuration.monnify;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.awazone.awazoneproject.exception.UnsuccessfulRequestException;
import net.awazone.awazoneproject.model.response.MonifyResponse;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.Map;

import static net.awazone.awazoneproject.configuration.monnify.MonifyCnfig.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;

@EnableScheduling
@ConfigurationProperties(prefix = "monify")
@Getter
@Slf4j
@Setter
public class MonifyCredentials {

    private String key;
    private String secrete;
    private String accessToken;

    private static final String HMAC_SHA512 = "HmacSHA512";

    public String base64Secrete() {
        return BASIC_AUTHORIZATION_PREFIX + Base64.getEncoder().encodeToString((getKey()+":"+getSecrete()).getBytes());
    }

    @Async
    @SuppressWarnings("unchecked")
    @Scheduled(fixedDelay = 2500000, initialDelay = 1000)
    public void setAccessToken() {
        WebClient webClient = WebClient.create(MONIFY_BASE_URL);

        MonifyResponse monifyResponse = webClient
                .post()
                .uri(LOGIN_URL)
                .accept(APPLICATION_JSON)
                .header("Authorization", base64Secrete())
                .retrieve()
                .bodyToMono(MonifyResponse.class)
                .blockOptional()
                .orElseThrow(() -> new UnsuccessfulRequestException("Failed to connect to payment processor"));
        
        assert monifyResponse != null;
        Map<String, String> map = (Map<String, String>) monifyResponse.getResponseBody();
        setAccessToken(map.get("accessToken"));
        log.info("New authorization set");
    }

}
