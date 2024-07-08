package org.pucar.dristi.web.models;

import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RequestInfoWrapperTest {

    private RequestInfoWrapper requestInfoWrapper;

    @BeforeEach
    public void setup() {
        requestInfoWrapper = new RequestInfoWrapper();
    }

    @Test
    public void testGettersAndSetters() {
        RequestInfo requestInfo = new RequestInfo();

        requestInfoWrapper.setRequestInfo(requestInfo);

        Assertions.assertEquals(requestInfo, requestInfoWrapper.getRequestInfo());
    }

    // Add more test cases to achieve higher coverage and edge cases

}
