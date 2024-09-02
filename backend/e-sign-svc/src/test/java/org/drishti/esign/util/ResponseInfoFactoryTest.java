package org.drishti.esign.util;


import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class ResponseInfoFactoryTest {

    public ResponseInfoFactoryTest() {
    }

    @Test
    public void testCreateResponseInfoFromRequestInfo_Success() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setApiId("apiId");
        requestInfo.setVer("v1");
        requestInfo.setTs(123456789L);
        requestInfo.setMsgId("msgId");

        ResponseInfo responseInfo = ResponseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);

        assertNotNull(responseInfo);
        assertEquals("apiId", responseInfo.getApiId());
        assertEquals("v1", responseInfo.getVer());
        assertEquals(123456789L, responseInfo.getTs());
        assertEquals("msgId", responseInfo.getMsgId());
        assertEquals("successful", responseInfo.getStatus());
    }

    @Test
    public void testCreateResponseInfoFromRequestInfo_Failure() {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setApiId("apiId");
        requestInfo.setVer("v1");
        requestInfo.setTs(123456789L);
        requestInfo.setMsgId("msgId");

        ResponseInfo responseInfo = ResponseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);

        assertNotNull(responseInfo);
        assertEquals("apiId", responseInfo.getApiId());
        assertEquals("v1", responseInfo.getVer());
        assertEquals(123456789L, responseInfo.getTs());
        assertEquals("msgId", responseInfo.getMsgId());
        assertEquals("failed", responseInfo.getStatus());
    }

    @Test
    public void testCreateResponseInfoFromRequestInfo_NullRequestInfo() {
        ResponseInfo responseInfo = ResponseInfoFactory.createResponseInfoFromRequestInfo(null, true);

        assertNotNull(responseInfo);
        assertEquals("", responseInfo.getApiId());
        assertEquals("", responseInfo.getVer());
        assertNull(responseInfo.getTs());
        assertEquals("", responseInfo.getMsgId());
        assertEquals("successful", responseInfo.getStatus());
    }

    @Test
    public void testCreateResponseInfoFromRequestInfo_PartialRequestInfo() {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setApiId("apiId");

        ResponseInfo responseInfo = ResponseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);

        assertNotNull(responseInfo);
        assertEquals("apiId", responseInfo.getApiId());
        assertNull(responseInfo.getTs());
        assertEquals("successful", responseInfo.getStatus());
    }
}