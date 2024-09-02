package org.pucar.dristi.util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class ResponseInfoFactoryTest {

    @InjectMocks
    private ResponseInfoFactory responseInfoFactory;

    @Mock
    private RequestInfo requestInfo;


    @BeforeEach
    void setUp() {
        // Initialization for any common setup can be done here
    }

    @Test
    void testCreateResponseInfoFromRequestInfoSuccess() {
        // Arrange
        when(requestInfo.getApiId()).thenReturn("apiId123");
        when(requestInfo.getVer()).thenReturn("v1");
        when(requestInfo.getTs()).thenReturn(1625244000000L);
        when(requestInfo.getMsgId()).thenReturn("msgId123");

        // Act
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);

        // Assert
        assertNotNull(responseInfo);
        assertEquals("apiId123", responseInfo.getApiId());
        assertEquals("v1", responseInfo.getVer());
        assertEquals(1625244000000L, responseInfo.getTs());
        assertEquals("uief87324", responseInfo.getResMsgId());
        assertEquals("msgId123", responseInfo.getMsgId());
        assertEquals("successful", responseInfo.getStatus());
    }

    @Test
    void testCreateResponseInfoFromRequestInfoFailure() {
        // Arrange
        when(requestInfo.getApiId()).thenReturn("apiId123");
        when(requestInfo.getVer()).thenReturn("v1");
        when(requestInfo.getTs()).thenReturn(1625244000000L);
        when(requestInfo.getMsgId()).thenReturn("msgId123");

        // Act
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);

        // Assert
        assertNotNull(responseInfo);
        assertEquals("apiId123", responseInfo.getApiId());
        assertEquals("v1", responseInfo.getVer());
        assertEquals(1625244000000L, responseInfo.getTs());
        assertEquals("uief87324", responseInfo.getResMsgId());
        assertEquals("msgId123", responseInfo.getMsgId());
        assertEquals("failed", responseInfo.getStatus());
    }

    @Test
    void testCreateResponseInfoFromRequestInfoWithNullRequestInfo() {
        // Act
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(null, true);

        // Assert
        assertNotNull(responseInfo);
        assertEquals("", responseInfo.getApiId());
        assertEquals("", responseInfo.getVer());
        assertNull(responseInfo.getTs());
        assertEquals("uief87324", responseInfo.getResMsgId());
        assertEquals("", responseInfo.getMsgId());
        assertEquals("successful", responseInfo.getStatus());
    }
}

