package org.pucar.dristi.web.models;

import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class EvidenceRequestTest {

    private EvidenceRequest evidenceRequest;

    @Mock
    private Artifact artifact;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        evidenceRequest = new EvidenceRequest();
    }

    @Test
    public void testAddArtifact() {
        evidenceRequest.addArtifact(artifact);
        Assertions.assertEquals(artifact, evidenceRequest.getArtifact());
    }

    @Test
    public void testGettersAndSetters() {
        RequestInfo requestInfo = new RequestInfo();
        evidenceRequest.setRequestInfo(requestInfo);

        Assertions.assertEquals(requestInfo, evidenceRequest.getRequestInfo());
    }

    // Add more test cases to achieve higher coverage and edge cases

}

