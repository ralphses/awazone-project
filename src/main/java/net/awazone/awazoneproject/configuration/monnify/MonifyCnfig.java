package net.awazone.awazoneproject.configuration.monnify;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MonifyCnfig {

    public static final String MONIFY_BASE_URL = "https://sandbox.monnify.com";
    public static final String LOGIN_URL = "/api/v1/auth/login";
    public static final String CREATE_VIRTUAL_ACCOUNT_URL = "/api/v2/bank-transfer/reserved-accounts";
    public static final String UPDATE_ACCOUNT_BVN_URL = "/api/v1/bank-transfer/reserved-accounts/update-customer-bvn/";
    public static final String FETCH_VIRTUAL_ACCOUNT_URL = "/api/v2/bank-transfer/reserved-accounts/";
    public static final String DEACTIVATE_VIRTUAL_ACCOUNT_URL = "/api/v1/bank-transfer/reserved-accounts/reference/";
    public static final String GET_ACCOUNT_TRANSACTION_DETAIL = "/api/v1/bank-transfer/reserved-accounts/transactions";
    public static final String GET_ACCOUNT_TRANSACTION_DETAIL_WITH_PAGE_SIZE = "/api/v1/bank-transfer/reserved-accounts/transactions?accountReference=%s&page=%d&size=%d";
    public static final String SINGLE_TRANSFER_URL = "/api/v1/bank-transfer/reserved-accounts/reference/";
    public static final String INIT_TRANSACTION_URL = "/api/v1/merchant/transactions/init-transaction";
    public static final String BANK_TRANSFER_URL = "/api/v1/merchant/bank-transfer/init-payment";
    public static final String TRANSACTION_STATUS = "/api/v2/transactions/";
    public static final String VALIDATE_BVN = "https://api.monnify.com/api/v1/vas/bvn-details-match";
    public static final String GET_BANKS_URL = "/api/v1/sdk/transactions/banks";
    public static final String PAY_WITH_CARD_URL = "/api/v1/merchant/cards/charge";
    public static final String[] ALLOWED_PAYMENT_METHODS = {"CARD","ACCOUNT_TRANSFER"};

    public static final String AUTHORIZATION_PREFIX = "Bearer ";
    public static final String BASIC_AUTHORIZATION_PREFIX = "Basic ";

    public static final String CONTRACT_CODE = "0840919684";
    public static final String NGN_CURRENCY_CODE = "NGN";
    public static final String WEMA_BANK_CODE = "035";



    @Bean
    MonifyCredentials monifyCredentials() {
        return new MonifyCredentials();
    }

}
