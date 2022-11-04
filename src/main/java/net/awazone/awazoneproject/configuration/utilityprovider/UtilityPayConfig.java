package net.awazone.awazoneproject.configuration.utilityprovider;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;


@Configuration
@Getter
public class UtilityPayConfig {

    @Value("${mobileNg.public_key}")
    private String publicKey;

    @Value("${mobileNg.secrete_key}")
    private String secreteKey;

    public static final String MOBILE_NG_BASE_URL = "https://enterprise.mobilenig.com";
    public static final String MOBILE_NG_AVAILABLE_DATA_BUNDLES_URI = "/api/services/packages";
    public static final String MOBILE_NG_DATA_BUNDLES_STATUS_URI = "/api/services/proxy";
    public static final String MOBILE_NG_BUY_DATA_BUNDLES_URI = "/api/services/";
    public static final String MOBILE_NG_SME_DATA_REQUEST_CODE = "SME";
    public static final String MOBILE_NG_GIFTING_DATA_REQUEST_CODE = "GIFTING";

    //MTN Data Services
    public static final String MOBILE_NG_MTN_DATA_SERVICE_ID_CODE = "BCA";

    //9Mobile Data Services
    public static final String MOBILE_NG_9MOBILE_DATA_SERVICE_ID_CODE = "BCB";

    //Glo Data Services
    public static final String MOBILE_NG_GLO_DATA_SERVICE_ID_CODE = "BCC";
}
