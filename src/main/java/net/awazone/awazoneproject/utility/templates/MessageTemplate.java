package net.awazone.awazoneproject.utility.templates;

public class MessageTemplate {

    public static String otp(String otp) {
        return "Hello\n" +
                "Your One-Time-Password is " + otp;
    }

    public static String verificationTokenPhone(String token, String tokenUrl) {
        return "Hello\n" +
                "Your verification code is " + token + "\n" +
                "Visit " + tokenUrl + "to verify directly in a web browser";
    }

    public static String signUpEmail(String fullName, String link) {

        return "Click here to activate " + link;
    }

    public static String passwordResetEmail(String fullName, String link) {

        return "Click here to reset your password " + link;
    }
}
