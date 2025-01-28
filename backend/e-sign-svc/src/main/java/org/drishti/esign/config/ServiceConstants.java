package org.drishti.esign.config;

import org.springframework.stereotype.Component;

@Component
public class ServiceConstants {

    public static final String EXTERNAL_SERVICE_EXCEPTION = "External Service threw an Exception: ";
    public static final String SEARCHER_SERVICE_EXCEPTION = "Exception while fetching from searcher: ";

    public static final String RES_MSG_ID = "uief87324";
    public static final String SUCCESSFUL = "successful";
    public static final String FAILED = "failed";

    public static final String X_509 = "X.509";
    public static final String UTF_8 = "UTF-8";
    public static final String SHA1_WITH_RSA = "SHA1withRSA";
    public static final String RSA = "RSA";
    public static final String PRIVATE_KEY_FILE_NAME = "privateKey.pem";
    public static final String PUBLIC_KEY_FILE_NAME = "testasp.cer";

    public static final String FONT_FAMILY = "Helvetica";
    public static final String FONT_STYLE = "italic";
    public static final String FILE_NAME = "signedDoc.pdf";
    public static final String FILE_STORE_SERVICE_EXCEPTION_CODE = "FILESTORE_SERVICE_EXCEPTION";
    public static final String FILE_STORE_SERVICE_EXCEPTION_MESSAGE = "exception occurred while calling filestore service";


}
