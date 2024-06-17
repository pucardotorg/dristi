package org.pucar.dristi.util;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ResponseInfoFactoryTest {

    private ResponseInfoFactory responseInfoFactory;

    @BeforeEach
    public void setUp() {
        responseInfoFactory = new ResponseInfoFactory();
    }

    @Test
    public void testCreateResponseInfoFromRequestInfo_Success() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setApiId("apiId");
        requestInfo.setVer("v1");
        requestInfo.setTs(123456789L);
        requestInfo.setMsgId("msgId");

        // Act
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);

        // Assert
        assertNotNull(responseInfo);
        assertEquals("apiId", responseInfo.getApiId());
        assertEquals("v1", responseInfo.getVer());
        assertEquals(123456789L, responseInfo.getTs());
        assertEquals("msgId", responseInfo.getMsgId());
        assertEquals("successful", responseInfo.getStatus());
    }

    @Test
    public void testCreateResponseInfoFromRequestInfo_Failure() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setApiId("apiId");
        requestInfo.setVer("v1");
        requestInfo.setTs(123456789L);
        requestInfo.setMsgId("msgId");

        // Act
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);

        // Assert
        assertNotNull(responseInfo);
        assertEquals("apiId", responseInfo.getApiId());
        assertEquals("v1", responseInfo.getVer());
        assertEquals(123456789L, responseInfo.getTs());
        assertEquals("msgId", responseInfo.getMsgId());
        assertEquals("failed", responseInfo.getStatus());
    }

    @Test
    public void testCreateResponseInfoFromRequestInfo_NullRequestInfo() {
        // Act
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(null, true);

        // Assert
        assertNotNull(responseInfo);
        assertEquals("", responseInfo.getApiId());
        assertEquals("", responseInfo.getVer());
        assertEquals(null, responseInfo.getTs());
        assertEquals("", responseInfo.getMsgId());
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
        assertEquals(null, responseInfo.getTs());
        assertEquals("successful", responseInfo.getStatus());
    }
}
