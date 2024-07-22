package org.pucar.dristi.service;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.web.models.VcCredentialRequest;
import org.springframework.util.StreamUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileDownloadServiceTest {

    @Mock
    private Configuration configuration;

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
