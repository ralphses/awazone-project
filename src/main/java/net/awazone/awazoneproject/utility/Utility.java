package net.awazone.awazoneproject.utility;

import javax.servlet.http.HttpServletRequest;
import java.security.SecureRandom;

public class Utility {

    public Long phoneTokenString() {
        SecureRandom secureRandom = new SecureRandom();
        return (long) secureRandom.nextInt(100000, 1000000);
    }

    public String applicationUrl(HttpServletRequest httpServletRequest) {
        return "http://" + httpServletRequest.getServerName()+":"+httpServletRequest.getServerPort() + httpServletRequest.getContextPath();
    }
}
