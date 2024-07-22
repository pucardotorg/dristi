package org.pucar.dristi.service;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileDownloadServiceTest {

    @InjectMocks
    @Spy
    private FileDownloadService fileDownloadService;


    @Test
    void testGetS3Url_Failure() {
        String apiUrl = "http://apiurl";
        String authToken = "authToken";
        String tenantId = "tenantId";

        assertThrows(
                CustomException.class,
                () -> fileDownloadService.getS3Url(apiUrl, authToken, tenantId),
                "Expected getS3Url() to throw, but it didn't"
        );
    }

    @Test
    void testDownloadFileFromS3_Failure() {
        String s3Url = "http://s3url";

        assertThrows(
                CustomException.class,
                () -> fileDownloadService.downloadFileFromS3(s3Url),
                "Expected downloadFileFromS3() to throw, but it didn't"
        );
    }

    @Test
    void testExtractSignature_Failure() {
        File pdfFile = mock(File.class);

        assertThrows(
                CustomException.class,
                () -> fileDownloadService.extractSignature(pdfFile),
                "Expected extractSignature() to throw, but it didn't"
        );
    }
}
