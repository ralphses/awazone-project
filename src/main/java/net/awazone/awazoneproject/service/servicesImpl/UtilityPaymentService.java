package net.awazone.awazoneproject.service.servicesImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import net.awazone.awazoneproject.configuration.utilityprovider.UtilityPayConfig;
import net.awazone.awazoneproject.exception.*;
import net.awazone.awazoneproject.model.aibopay.UserWallet;
import net.awazone.awazoneproject.model.mobileUtility.*;
import net.awazone.awazoneproject.model.response.DataStatusResponse;
import net.awazone.awazoneproject.model.response.RequestInformation;
import net.awazone.awazoneproject.service.serviceInterfaces.aibopay.WalletService;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.*;

import static net.awazone.awazoneproject.configuration.utilityprovider.UtilityPayConfig.*;
import static org.springframework.http.MediaType.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UtilityPaymentService {

    private final WalletService walletService;

    private final UtilityPayConfig utilityPayConfig;

    public List<DataPackage> getAvailableData() {
        Map<String, DataAvailableResponse> availableDataBundles = getAvailableDataBundles();
        return getAvailableDataPackages(availableDataBundles);
    }

    public Object buyData(BuyDataRequest buyDataRequest) {

        String networkType = buyDataRequest.getType();

        if(!networkType.startsWith("B")) {
            throw new CustomInvalidParamException("Invalid network type passed " + networkType);
        }

        //Todo: find user
        String email = buyDataRequest.getEmail();
        double cost = Double.parseDouble(buyDataRequest.getCost());

        checkBalance(email, cost);

        String[] networkAndType = networkType.split("_");

        BuyDataRequestBody buyDataRequestBody = BuyDataRequestBody.builder()
                .service_id(networkAndType[0])
                .service_type(networkAndType[1])
                .amount((int) cost)
                .beneficiary(buyDataRequest.getPhoneNumber())
                .code(buyDataRequest.getCode())
                .trans_id(getTransId())
                .build();

        MobileUtilityResponse mobileUtilityResponse = (MobileUtilityResponse) sendRequest(
                        MOBILE_NG_BUY_DATA_BUNDLES_URI,
                        HttpMethod.POST,
                        buyDataRequestBody,
                        utilityPayConfig.getSecreteKey());

        return mobileUtilityResponse.getDetails();
    }

    public Object buyAirtime(BuyAirtimeRequest buyAirtimeRequest) {

        checkBalance(buyAirtimeRequest.getEmail(), buyAirtimeRequest.getAmount());

        if(getAirtimeServiceStatus(buyAirtimeRequest)) {

            BuyAirtimeRequestBody buyAirtimeRequestBody = BuyAirtimeRequestBody.builder()
                    .service_type("STANDARD")
                    .phoneNumber(buyAirtimeRequest.getPhoneNumber())
                    .service_id(buyAirtimeRequest.getService_id())
                    .amount(buyAirtimeRequest.getAmount())
                    .trans_id(getTransId())
                    .build();

            MobileUtilityResponse mobileUtilityResponse = (MobileUtilityResponse) sendRequest(
                    MOBILE_NG_BUY_DATA_BUNDLES_URI,
                    HttpMethod.POST,
                    buyAirtimeRequestBody,
                    utilityPayConfig.getSecreteKey());

            return  mobileUtilityResponse.getDetails();
        }
        throw new CustomInvalidParamException("Service temporary down");
    }

    private void checkBalance(String email, double cost) {
        UserWallet wallet = walletService.findByUsername(email);
        double currentBalance = (wallet.getAccount().getCurrentBalance()).doubleValue();

        if(Double.compare(currentBalance, cost) == -1) {
            throw new InsufficientResourceException("Insufficient fund") ;
        }
    }

    private String getTransId() {
        return "11"+UUID.randomUUID().toString().replace("-", "").substring(0, 13);
    }

    @SuppressWarnings("unchecked")
    private List<DataPackage> getAvailableDataPackages(Map<String, DataAvailableResponse> availableDataBundles) {

        List<DataPackage> dataPackages = new ArrayList<>();

        availableDataBundles.forEach((key, value) -> {

            if(value.getAvailable() == 1 && value.getStrength().equalsIgnoreCase("Excellent")) {
                StatusRequest statusRequest = StatusRequest.builder()
                        .requestType((key.equals("BCA")) ? MOBILE_NG_SME_DATA_REQUEST_CODE : MOBILE_NG_GIFTING_DATA_REQUEST_CODE)
                        .service_id(key)
                        .build();
                MobileUtilityResponse response = (MobileUtilityResponse) sendRequest(MOBILE_NG_AVAILABLE_DATA_BUNDLES_URI, HttpMethod.POST, statusRequest, utilityPayConfig.getPublicKey());

                ArrayList<DataPackage> data = new ObjectMapper().convertValue(response.getDetails(), ArrayList.class);
                dataPackages.addAll(data);
            }
        });

        return dataPackages;
    }

    private Map<String, DataAvailableResponse> getAvailableDataBundles() {

        Map<String, DataAvailableResponse> availableData = new HashMap<>();

        Map<String, String> dataSet = Map.of(
                MOBILE_NG_9MOBILE_DATA_SERVICE_ID_CODE, MOBILE_NG_GIFTING_DATA_REQUEST_CODE,
                MOBILE_NG_GLO_DATA_SERVICE_ID_CODE, MOBILE_NG_GIFTING_DATA_REQUEST_CODE,
                MOBILE_NG_MTN_DATA_SERVICE_ID_CODE, MOBILE_NG_SME_DATA_REQUEST_CODE
        );

        dataSet.forEach((key, value) -> {
            StatusRequest statusRequest = StatusRequest.builder()
                    .service_id(key)
                    .requestType(value)
                    .build();

            MobileUtilityResponse response = (MobileUtilityResponse) sendRequest(
                    MOBILE_NG_DATA_BUNDLES_STATUS_URI, HttpMethod.POST, statusRequest, utilityPayConfig.getPublicKey());

            DataAvailableResponse availableResponse = new ObjectMapper().convertValue(response.getDetails(), DataAvailableResponse.class);
            availableData.put(key, availableResponse);
        });

       return availableData;
    }


    private Object sendRequest(String url, HttpMethod method, Object requestBody, String authorization) {

        try {
            return
            WebClient.create(MOBILE_NG_BASE_URL)
                    .method(method)
                    .uri(url)
                    .contentType(APPLICATION_JSON)
                    .header("Authorization", "Bearer " + authorization.replace(" ", "+"))
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(HttpStatus::isError,
                            response -> switch (response.rawStatusCode()) {
                            case 400 -> Mono.error(new CustomInvalidParamException("Bad request made"));
                            case 401 -> Mono.error(new IllegalUserException("Service not available for user"));
                            case 402 -> Mono.error(new InsufficientResourceException("Insufficient wallet balance"));
                            case 417 -> Mono.error(new UnsuccessfulRequestException("Transaction failed or Insufficient"));
                            default -> Mono.error(new UnsuccessfulRequestException("Unexpected error occurred " + response.rawStatusCode()));
                            })
                    .bodyToMono(MobileUtilityResponse.class)
                    .block(Duration.ofSeconds(10));
        }catch (Exception exception) {
            throw new UtilityPayException(exception.getMessage());
        }
    }

    private boolean getAirtimeServiceStatus(BuyAirtimeRequest buyAirtimeRequest) {
        StatusRequest statusRequest = StatusRequest.builder()
                .requestType("STANDARD")
                .service_id(buyAirtimeRequest.getService_id())
                .build();

        //Todo: Check status
        MobileUtilityResponse mobileUtilityResponse =
                (MobileUtilityResponse) sendRequest(
                        MOBILE_NG_DATA_BUNDLES_STATUS_URI,
                        HttpMethod.POST,
                        statusRequest,
                        utilityPayConfig.getPublicKey());


        DataStatusResponse dataStatusResponse =
                new ObjectMapper().convertValue(mobileUtilityResponse.getDetails(), DataStatusResponse.class);

        RequestInformation data = dataStatusResponse.getData();

        return
                Objects.equals(dataStatusResponse.getMessage(), "success") &&
                Objects.equals(data.getStatus(), "Available") &&
                !data.isBlocked();
    }
}
