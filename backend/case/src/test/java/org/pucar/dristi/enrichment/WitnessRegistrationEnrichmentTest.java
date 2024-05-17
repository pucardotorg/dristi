package org.pucar.dristi.enrichment;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.pucar.dristi.util.IdgenUtil;
import org.pucar.dristi.util.UserUtil;
import org.pucar.dristi.web.models.Witness;
import org.pucar.dristi.web.models.WitnessRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

 class WitnessRegistrationEnrichmentTest {

    @InjectMocks
    private WitnessRegistrationEnrichment witnessRegistrationEnrichment;

    @Mock
    private IdgenUtil idgenUtil;

    @Mock
    private UserUtil userUtil;

    @BeforeEach
    public void setUp() {
        witnessRegistrationEnrichment = new WitnessRegistrationEnrichment();
    }

    @Test
     void testEnrichCaseRegistration_Success() {
        // Arrange
        WitnessRequest witnessRequest = new WitnessRequest();
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid(UUID.randomUUID().toString()); // Mocking UUID
        requestInfo.setUserInfo(userInfo);
        witnessRequest.setRequestInfo(requestInfo);

        List<Witness> witnesses = new ArrayList<>();
        Witness witness1 = new Witness();
        witnesses.add(witness1);
        Witness witness2 = new Witness();
        witnesses.add(witness2);
        witnessRequest.setWitnesses(witnesses);


        // Act
        witnessRegistrationEnrichment.enrichWitnessRegistration(witnessRequest);

        // Assert
        for (Witness witness : witnessRequest.getWitnesses()) {
            assertEquals(userInfo.getUuid(), witness.getAuditDetails().getCreatedBy());
            assertEquals(userInfo.getUuid(), witness.getAuditDetails().getLastModifiedBy());
            assertEquals(false, witness.getIsActive());
            assertEquals(witness.getFilingNumber(), witness.getCnrNumber());
            assertEquals(36, witness.getId().toString().length()); // Assuming UUID length is 36
            assertEquals(36, witness.getFilingNumber().length()); // Assuming UUID length is 36
        }
    }

    @Test
     void testEnrichWitnessApplicationUponUpdate_Success() {
        // Arrange
        WitnessRequest witnessRequest = new WitnessRequest();
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid(UUID.randomUUID().toString()); // Mocking UUID
        requestInfo.setUserInfo(userInfo);
        witnessRequest.setRequestInfo(requestInfo);

        List<Witness> witnesses = new ArrayList<>();
        Witness witness1 = new Witness();
        witnesses.add(witness1);
        Witness witness2 = new Witness();
        witnesses.add(witness2);
        witnessRequest.setWitnesses(witnesses);

        // Ensure that AuditDetails object is initialized for all witnesses
        for (Witness witness : witnessRequest.getWitnesses()) {
            if (witness.getAuditDetails() == null) {
                witness.setAuditDetails(new AuditDetails());
            }
        }

        // Act
        witnessRegistrationEnrichment.enrichWitnessApplicationUponUpdate(witnessRequest);

        // Assert
        for (Witness witness : witnessRequest.getWitnesses()) {
            assertEquals(userInfo.getUuid(), witness.getAuditDetails().getLastModifiedBy());
        }
    }

}

    // Add more test cases as necessary

