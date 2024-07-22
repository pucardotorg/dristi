package org.pucar.dristi.service;

import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.pucar.dristi.config.Configuration;

import java.io.File;
import java.io.IOException;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FileDownloadServiceTest {

    @Mock
    private Configuration configuration;

    @InjectMocks
    @Spy
    private FileDownloadService fileDownloadService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }


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
    void testExtractSignature_Failure() throws IOException {
        File pdfFile = mock(File.class);

        assertThrows(
                CustomException.class,
                () -> fileDownloadService.extractSignature(pdfFile),
                "Expected extractSignature() to throw, but it didn't"
        );
    }
}
