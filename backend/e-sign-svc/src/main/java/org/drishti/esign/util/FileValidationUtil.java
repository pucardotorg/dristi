package org.drishti.esign.util;

import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

public class FileValidationUtil {

    public static boolean isValidFile(MultipartFile file) {
        String contentType = file.getContentType();
        return contentType != null && (
                contentType.equals(MediaType.IMAGE_JPEG_VALUE) ||
                        contentType.equals(MediaType.IMAGE_PNG_VALUE) ||
                        contentType.equals(MediaType.APPLICATION_PDF_VALUE) ||
                        contentType.equals("application/vnd.oasis.opendocument.text") ||
                        contentType.equals("application/vnd.oasis.opendocument.spreadsheet") ||
                        contentType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ||
                        contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ||
                        contentType.equals("application/msword") ||
                        contentType.equals("application/vnd.ms-excel") ||
                        contentType.equals("application/dxf") ||
                        contentType.equals("text/csv") ||
                        contentType.equals("text/plain")
        );
    }
}
