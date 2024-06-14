package org.pucar.dristi.web.models;

import org.egov.common.contract.response.ResponseInfo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;

public class EvidenceResponseTest {

    private EvidenceResponse evidenceResponse;

    @Mock
    private Artifact artifact;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        evidenceResponse = new EvidenceResponse();
    }

    @Test
    public void testAddArtifact() {
        evidenceResponse.addArtifact(artifact);
        Assertions.assertEquals(artifact, evidenceResponse.getArtifact());
    }

    @Test
    public void testGettersAndSetters() {
        ResponseInfo responseInfo = new ResponseInfo();
        Pagination pagination = new Pagination();
        evidenceResponse.setResponseInfo(responseInfo);
        evidenceResponse.setPagination(pagination);

        Assertions.assertEquals(responseInfo, evidenceResponse.getResponseInfo());
        Assertions.assertEquals(pagination, evidenceResponse.getPagination());
    }

    // Add more test cases to achieve higher coverage and edge cases

}
