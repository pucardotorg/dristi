package org.pucar.dristi.util;

import java.security.SecureRandom;

import org.springframework.stereotype.Component;

@Component
public class CaseUtil {
    private static final String CHARACTERS = "0123456789";
    private static final SecureRandom random = new SecureRandom();

    public String getCNRNumber(String fillingNumber, String state, String district, String establishmentCode) {
        String cnrNumber;
        String[] resp = fillingNumber.split("-");
        String sequenceNumber = resp[resp.length-1];
        String year = resp[resp.length-2];
        cnrNumber = state+district+establishmentCode+"-"+sequenceNumber+"-"+year;

        return cnrNumber;
    }

    public static String generateAccessCode(int length) {
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(CHARACTERS.length());
            char randomChar = CHARACTERS.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }

    public Long getCurrentTimeMil() {
        return System.currentTimeMillis();
    }
}