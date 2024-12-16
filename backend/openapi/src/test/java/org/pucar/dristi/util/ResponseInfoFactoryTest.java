package org.pucar.dristi.util;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResponseInfoFactoryTest {

    private ResponseInfoFactory responseInfoFactory;

    @BeforeEach
    public void setUp() {
        responseInfoFactory = new ResponseInfoFactory();
    }

    private RequestInfo createTestRequestInfo() {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setApiId("apiId");
        requestInfo.setVer("v1");
        requestInfo.setTs(123456789L);
        requestInfo.setMsgId("msgId");
        return requestInfo;
    }

    @Test
    public void testCreateResponseInfoFromRequestInfo_Success() {
        // Arrange
        RequestInfo requestInfo = createTestRequestInfo();

        // Act
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);

        // Assert
        assertNotNull(responseInfo);
        assertEquals("apiId", responseInfo.getApiId());
        assertEquals("v1", responseInfo.getVer());
        assertEquals(123456789L, responseInfo.getTs());
        assertEquals("msgId", responseInfo.getMsgId());
        assertEquals("uief87324", responseInfo.getResMsgId());
        assertEquals("successful", responseInfo.getStatus());
    }

    @Test
    public void testCreateResponseInfoFromRequestInfo_Failure() {
        // Arrange
        RequestInfo requestInfo = createTestRequestInfo();

        // Act
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);

        // Assert
        assertNotNull(responseInfo);
        assertEquals("apiId", responseInfo.getApiId());
        assertEquals("v1", responseInfo.getVer());
        assertEquals(123456789L, responseInfo.getTs());
        assertEquals("msgId", responseInfo.getMsgId());
        assertEquals("uief87324", responseInfo.getResMsgId());
        assertEquals("failed", responseInfo.getStatus());
    }

    @Test
    public void testCreateResponseInfoFromRequestInfo_NullRequestInfo_ShouldReturnDefaultValues() {
        // Act
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(null, true);

        // Assert
        assertNotNull(responseInfo);
        assertEquals("", responseInfo.getApiId());
        assertEquals("", responseInfo.getVer());
        assertNull(responseInfo.getTs());
        assertEquals("", responseInfo.getMsgId());
        assertEquals("uief87324", responseInfo.getResMsgId());
        assertEquals("successful", responseInfo.getStatus());
    }

    @Test
    public void testCreateResponseInfoFromRequestInfo_PartialRequestInfo() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setApiId("apiId");

        // Act
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);

        // Assert
        assertNotNull(responseInfo);
        assertEquals("apiId", responseInfo.getApiId());
        assertNull(responseInfo.getTs());
        assertEquals("uief87324", responseInfo.getResMsgId());
        assertEquals("successful", responseInfo.getStatus());
    }
}