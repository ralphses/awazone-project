package net.awazone.awazoneproject.service.servicesImpl.aibopay;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class AccountServiceImplTest {


    @BeforeEach
    void setUp() {
    }
    
    @Test
    void checkDate() {
        LocalDateTime now = LocalDateTime.now();
        System.out.println("now = " + now);

        String date = "2019-07-24T14:12:28.000+0000".replace("+", "");
        String date2 = "2019-07-24T14:12:28.000+0000".replace("+", "");
        now = LocalDateTime.parse(date2);

        LocalDateTime newDate = LocalDateTime.parse(date);

        assertTrue(now.isEqual(newDate));
//        "2022-09-30 17:48:49.272"
//                "2022-09-30T17:51:04.447829400"
        
    }

    @Test
    void encode() {
        String value = "Ralphses1234";
        String encoded = Base64.getEncoder().encodeToString(value.getBytes());
        System.out.println("encoded = " + encoded);
        byte[] decode = Base64.getDecoder().decode(encoded);
        
        String s = new String(decode);

        System.out.println("s = " + s);

        assertArrayEquals(value.getBytes(), decode);

    }

    @Test
    public void validateKey() {
        assertEquals("$2y$10$BZUXypYCHfLlAQAEp5uq3uZ4byraajrVh6LnsEuZ8ILa5LWwNXIcq", "$2y$10$BZUXypYCHfLlAQAEp5uq3uZ4byraajrVh6LnsEuZ8ILa5LWwNXIcq");
    }

    @Test
    public void testDouble() {
        double first = 3.0;
        double second = 3.8;

        assertEquals(Double.compare(first, second), -1);


    }

    @Test
    public void testReferralCode() {
        String username = "myname";
        String substring = username + UUID.randomUUID().toString().replace("-", "").substring(0, 5);
        System.out.println("substring = " + substring);
    }

}