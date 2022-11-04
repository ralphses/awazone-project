package net.awazone.awazoneproject.service.servicesImpl.aibopay;

import lombok.AllArgsConstructor;
import net.awazone.awazoneproject.configuration.coinremmita.CoinRemitterCredential;
import net.awazone.awazoneproject.exception.CoinRemmitterException;
import net.awazone.awazoneproject.exception.UnsuccessfulRequestException;
import net.awazone.awazoneproject.model.dtos.aibopay.coinremmitter.CheckInvoiceStatusRequest;
import net.awazone.awazoneproject.model.dtos.aibopay.coinremmitter.CoinRemmitterResponse;
import net.awazone.awazoneproject.model.dtos.aibopay.coinremmitter.NewCryptoPaymentRequest;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

import static net.awazone.awazoneproject.configuration.coinremmita.CoinRemitterCredential.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;


@Service
@Transactional
@AllArgsConstructor
public class CoinRemmitaPaymentProcessor {

    public Map<String, Object> processPayment(String coinType, NewCryptoPaymentRequest newCryptoPaymentRequest) {
        String url = String.format(CREATE_INVOICE_URL, coinType);

        CoinRemmitterResponse coinRemmitterResponse = sendRequest(url, POST, newCryptoPaymentRequest);

        if(coinRemmitterResponse.getFlag() != 1) {
            throw new UnsuccessfulRequestException("Request not successful");
        }

        return coinRemmitterResponse.getData();
    }

    public String checkInvoiceStatus(String invoiceId, String coinType) {

        String requestUrl = String.format(CHECK_INVOICE_STATUS_URL, coinType);
        CheckInvoiceStatusRequest checkInvoiceStatusRequest = CheckInvoiceStatusRequest.builder()
                .invoice_id(invoiceId)
                .api_key(TCN_API_KEY)
                .password(PASSWORD)
                .build();

        CoinRemmitterResponse coinRemmitterResponse = sendRequest(requestUrl, POST, checkInvoiceStatusRequest);

        if(coinRemmitterResponse.getFlag() != 1) {
            throw new UnsuccessfulRequestException("Request not successful");
        }

        return (String) coinRemmitterResponse.getData().get("status");
    }

    private CoinRemmitterResponse sendRequest(String url, HttpMethod method, Object requestBody) {

        try {
            return WebClient.create(CoinRemitterCredential.BASE_URL)
                    .method(method)
                    .uri(url)
                    .bodyValue(requestBody)
                    .accept(APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(CoinRemmitterResponse.class)
                    .blockOptional()
                    .orElseThrow(() -> new UnsuccessfulRequestException("Could not connect to payment processor"));

        }catch (Exception exception) {
            throw new CoinRemmitterException(exception.getMessage());
        }
    }
}
