package drishti.payment.calculator.util;

import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
public class ResponseInfoFactoryTest {

    @InjectMocks
    private ResponseInfoFactory responseInfoFactory;

    @Test
    public void testCreateResponseInfoFromRequestInfo_Success() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setApiId("apiId");
        requestInfo.setVer("v1");
        requestInfo.setTs(123456789L);
        requestInfo.setMsgId("msgId");

        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);

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

        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, false);

        assertNotNull(responseInfo);
        assertEquals("apiId", responseInfo.getApiId());
        assertEquals("v1", responseInfo.getVer());
        assertEquals(123456789L, responseInfo.getTs());
        assertEquals("msgId", responseInfo.getMsgId());
        assertEquals("failed", responseInfo.getStatus());
    }

    @Test
    public void testCreateResponseInfoFromRequestInfo_NullRequestInfo() {
        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(null, true);

        assertNotNull(responseInfo);
        assertEquals("", responseInfo.getApiId());
        assertEquals("", responseInfo.getVer());
        assertEquals(null, responseInfo.getTs());
        assertEquals("", responseInfo.getMsgId());
        assertEquals("successful", responseInfo.getStatus());
    }

    @Test
    public void testCreateResponseInfoFromRequestInfo_PartialRequestInfo() {
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setApiId("apiId");

        ResponseInfo responseInfo = responseInfoFactory.createResponseInfoFromRequestInfo(requestInfo, true);

        assertNotNull(responseInfo);
        assertEquals("apiId", responseInfo.getApiId());
        assertEquals(null, responseInfo.getTs());
        assertEquals("successful", responseInfo.getStatus());
    }
}
