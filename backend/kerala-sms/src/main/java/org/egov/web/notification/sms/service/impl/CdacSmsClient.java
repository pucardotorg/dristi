package org.egov.web.notification.sms.service.impl;

/**
 *
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.KeyManagementException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.net.ssl.SSLContext;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.egov.web.notification.sms.config.SMSProperties;
import org.egov.web.notification.sms.models.Sms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * @author Mobile Seva < msdp@cdac.in >
 * <p>
 * Kindly add require Jar files to run Java client
 * <p>
 * Apache commons-codec-1.9
 * <p>
 * Apache commons-httpclient-3.1
 * <p>
 * Apache commons-logging-1.2
 * @see
 */
@Component
@Slf4j
public class CdacSmsClient {

    @Autowired
    private RestTemplate restTemplate;

    /**
     * Send Single text SMS
     * @return {@link String} response from Mobile Seva Gateway e.g. '402,MsgID = 150620161466003974245msdgsms'
     * @see
     */

    public String sendSingleSMS(Sms sms, SMSProperties smsProperties)
    {
        return sendSMS(sms, smsProperties,false, "singlemsg");
    }

    /**
     * Send Bulk text SMS
     * @return {@link String} response from Mobile Seva Gateway e.g. '402,MsgID = 150620161466003974245msdgsms'
     * @see
     */
    public String sendBulkSMS(Sms sms, SMSProperties smsProperties)
    {
        return sendSMS(sms, smsProperties, true, "bulkmsg");
    }

    /**
     * Send Unicode text SMS
     * @return {@link String} response from Mobile Seva Gateway e.g. '402,MsgID = 150620161466003974245msdgsms'
     * @see
     */
    public String sendUnicodeSMS(Sms sms, SMSProperties smsProperties)
    {
        String message = sms.getMessage();
        String finalmessage = "";
        for (int i = 0; i < message.length(); i++) {

            char ch = message.charAt(i);
            int j = (int) ch;
            String sss = "&#" + j + ";";
            finalmessage = finalmessage + sss;
        }

        return sendSMS(sms, smsProperties, true, "unicodemsg");
    }

    /**
     * Send Single OTP text SMS
     * <p>
     * Use only in case of OTP related message
     * <p>
     * Messages other than OTP will not be delivered to the users
     * @return {@link String} response from Mobile Seva Gateway e.g. '402,MsgID = 150620161466003974245msdgsms'
     * @see
     */
    public String sendOtpSMS(Sms sms, SMSProperties smsProperties)
    {
        return sendSMS(sms, smsProperties,false, "otpmsg");
    }

    /**
     * Send Single Unicode OTP text SMS
     * @return {@link String} response from Mobile Seva Gateway e.g. '402,MsgID = 150620161466003974245msdgsms'
     * @see
     */
    public String sendUnicodeOtpSMS(Sms sms, SMSProperties smsProperties)
    {
        String message = sms.getMessage();
        String finalmessage = "";
        for (int i = 0; i < message.length(); i++) {

            char ch = message.charAt(i);
            int j = (int) ch;
            String sss = "&#" + j + ";";
            finalmessage = finalmessage + sss;
        }

        return sendSMS(sms, smsProperties, false, "unicodeotpmsg");
    }


    public String sendSMS(Sms sms, SMSProperties smsProperties, boolean isBulk, String smsServiceType)
    {
        String smsProviderURL = smsProperties.getUrl();
        String username = smsProperties.getUsername();
        String password = smsProperties.getPassword();
        String senderId = smsProperties.getSenderid();
        String secureKey = smsProperties.getSecureKey();

        String mobileNumber = sms.getMobileNumber();
        String message = sms.getMessage();
        String templateId = sms.getTemplateId();

        String responseString = "";
        SSLConnectionSocketFactory scf;
        SSLContext context = null;
        String encryptedPassword = "";

        try
        {
            context = SSLContext.getInstance("TLSv1.2"); // Use this line for Java version 7 and above
            context.init(null, null, null);
            scf = new SSLConnectionSocketFactory(context);

            HttpClient httpClient = HttpClients.custom()
                    .setSSLSocketFactory(scf)
                    .build();

            restTemplate = createRestTemplate(httpClient);

            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            encryptedPassword = MD5(password);
            String genratedhashKey = hashGenerator(username, senderId, message, secureKey);

            MultiValueMap<String, String> requestBodyMap = new LinkedMultiValueMap<>();

            if (!isBulk) requestBodyMap.add("mobileno", mobileNumber);
            else requestBodyMap.add("bulkmobno", mobileNumber);

            requestBodyMap.add("senderid", senderId);
            requestBodyMap.add("content", message);
            requestBodyMap.add("smsservicetype", smsServiceType);
            requestBodyMap.add("username", username);
            requestBodyMap.add("password", encryptedPassword);
            requestBodyMap.add("key", genratedhashKey);
            requestBodyMap.add("templateid", templateId);
            log.info("Request Body Map: {}", requestBodyMap);
            log.info("Request Url: {}", smsProviderURL);

            HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBodyMap, httpHeaders);
            ResponseEntity<String> responseEntity = restTemplate.exchange(smsProviderURL, HttpMethod.POST, requestEntity, String.class);

            log.info(responseEntity.getBody().toString());
            responseString = responseEntity.getBody().toString();
        }
        catch (NoSuchAlgorithmException | KeyManagementException | IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }

        return responseString;
    }

    protected String hashGenerator(String userName, String senderId, String content, String secureKey) {
// TODO Auto-generated method stub
        StringBuffer finalString = new StringBuffer();
        finalString.append(userName.trim()).append(senderId.trim()).append(content.trim()).append(secureKey.trim());
// logger.info("Parameters for SHA-512 : "+finalString);
        String hashGen = finalString.toString();
        StringBuffer sb = null;
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-512");
            md.update(hashGen.getBytes());
            byte byteData[] = md.digest();
//convert the byte to hex format method 1
            sb = new StringBuffer();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }

        } catch (NoSuchAlgorithmException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
            log.error(e.getMessage(), e);
        }
        return sb.toString();
    }

    /**
     * Get units of the unicode message
     *
     * @param message e.g. 'à¤µà¤¿à¤•à¤¾à¤¸ à¤†à¤£à¤¿ à¤ªà¥à¤°à¤—à¤¤ à¤¸à¤‚à¤—à¤£à¤¨ à¤•à¥‡à¤‚à¤¦à¥à¤° à¤®à¤§à¥à¤¯à¥‡ à¤¸à¥à¤µà¤¾à¤—à¤¤ à¤†à¤¹à¥‡'
     * @return int message unit
     **/
    public int getUnicodeTextMessageUnit(String message) {
        String charInUnicode = "";
        int msgUnit = 1;
        int msgLen = 0;
        String unicodeMessgae = "";
        String finalMessage = null;
        for (int i = 0; i < message.length(); i++) {
            char ch = message.charAt(i);
            finalMessage = finalMessage + "&#" + (int) ch + ";";
        }
        StringTokenizer stringTokenizer = new StringTokenizer(finalMessage, " ");
        while (stringTokenizer.hasMoreElements()) {
            String str1 = (String) stringTokenizer.nextElement();
            StringTokenizer tmpStringTokenizer = new StringTokenizer(str1, ";");

            while (tmpStringTokenizer.hasMoreElements()) {
                charInUnicode = (String) tmpStringTokenizer.nextElement();
                if (charInUnicode.startsWith("&#")) {
                    StringTokenizer df = new StringTokenizer(
                            charInUnicode, "&#");
                    while (df.hasMoreElements()) {
                        unicodeMessgae = unicodeMessgae + "," + df.nextElement();
                        msgLen = msgLen + 1;
                    }

                } else {
                    if (charInUnicode.contains("&#")) {
                        StringTokenizer stringTokenizer1 = new StringTokenizer(charInUnicode, "&#");
                        while (stringTokenizer1.hasMoreElements()) {
                            String tmpStr = (String) stringTokenizer1.nextElement();
                            for (int i1 = 0; i1 < tmpStr.length(); ++i1) {
                                char c = tmpStr.charAt(i1);
                                int j = (int) c;
                                unicodeMessgae = unicodeMessgae + "," + j;
                                msgLen = msgLen + 1;
                            }
                            String uni = stringTokenizer1.nextToken();
                            unicodeMessgae = unicodeMessgae + "," + uni;
                            msgLen = msgLen + 1;
                        }
                    } else {
                        for (int i1 = 0; i1 < charInUnicode.length(); ++i1) {
                            char c = charInUnicode.charAt(i1);
                            int j = (int) c;
                            unicodeMessgae = unicodeMessgae + "," + j;
                            msgLen = msgLen + 1;
                        }
                    }
                }

            }
            unicodeMessgae = unicodeMessgae + " ";
        }

        if (msgLen > 70 && msgLen <=134) msgUnit = 2;
        else if (msgLen <= 201) msgUnit = 3;
        else if (msgLen <= 268) msgUnit = 4;
        else if (msgLen <= 335) msgUnit = 5;
        else if (msgLen <= 402) msgUnit = 6;
        else if (msgLen <= 469) msgUnit = 7;
        else if (msgLen <= 536) msgUnit = 8;
        else if (msgLen <= 603) msgUnit = 9;
        else msgUnit = 10;

        return msgUnit;
    }

    /**
     * Get units of the text message
     *
     * @param message e.g. 'Welcome to Mobile Seva'
     * @return int message unit
     **/
    public int getNormalTextMessageUnit(String message) {

        int msgUnit = 1;

        int msgLen = message.length();

        if (msgLen > 160 && msgLen <=306) msgUnit = 2;
        else if (msgLen <= 459) msgUnit = 3;
        else if (msgLen <= 612) msgUnit = 4;
        else if (msgLen <= 765) msgUnit = 5;
        else if (msgLen <= 918) msgUnit = 6;
        else if (msgLen <= 1071) msgUnit = 7;
        else if (msgLen <= 1224) msgUnit = 8;
        else if (msgLen <= 1377) msgUnit = 9;
        else msgUnit = 10;

        return msgUnit;
    }


    /****
     * Method to convert Normal Plain Text Password to MD5 encrypted password
     ***/

    private static String MD5(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        MessageDigest md;
        md = MessageDigest.getInstance("SHA-1");
        byte[] md5 = new byte[64];
        md.update(text.getBytes("iso-8859-1"), 0, text.length());
        md5 = md.digest();
        return convertedToHex(md5);
    }

    private static String convertedToHex(byte[] data) {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < data.length; i++) {
            int halfOfByte = (data[i] >>> 4) & 0x0F;
            int twoHalfBytes = 0;

            do {
                if ((0 <= halfOfByte) && (halfOfByte <= 9)) {
                    buf.append((char) ('0' + halfOfByte));
                } else {
                    buf.append((char) ('a' + (halfOfByte - 10)));
                }

                halfOfByte = data[i] & 0x0F;

            } while (twoHalfBytes++ < 1);
        }
        return buf.toString();
    }

    public RestTemplate createRestTemplate(HttpClient httpClient) {
        ClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory(httpClient);
        return new RestTemplate(requestFactory);
    }

}
