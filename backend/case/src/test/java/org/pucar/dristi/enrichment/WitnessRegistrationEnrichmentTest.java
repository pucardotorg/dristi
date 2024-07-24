package org.pucar.dristi.enrichment;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.UUID;

import org.egov.common.contract.models.AuditDetails;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.pucar.dristi.web.models.Witness;
import org.pucar.dristi.web.models.WitnessRequest;

public class WitnessRegistrationEnrichmentTest {

    @InjectMocks
    private WitnessRegistrationEnrichment witnessRegistrationEnrichment;

    @BeforeEach
    public void setUp() {
        witnessRegistrationEnrichment = new WitnessRegistrationEnrichment();
    }

    @Test
    public void testEnrichCaseRegistration_Success() {
        // Arrange
        WitnessRequest witnessRequest = new WitnessRequest();
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid(UUID.randomUUID().toString()); // Mocking UUID
        requestInfo.setUserInfo(userInfo);
        witnessRequest.setRequestInfo(requestInfo);

        Witness witness = new Witness();
        witnessRequest.setWitness(witness);


        // Act
        witnessRegistrationEnrichment.enrichWitnessRegistration(witnessRequest);

        // Assert
        assertEquals(userInfo.getUuid(), witness.getAuditDetails().getCreatedBy());
        assertEquals(userInfo.getUuid(), witness.getAuditDetails().getLastModifiedBy());
        assertEquals(false, witness.getIsActive());
        assertEquals(witness.getFilingNumber(), witness.getCnrNumber());
        assertEquals(36, witness.getId().toString().length()); // Assuming UUID length is 36
        assertEquals(36, witness.getFilingNumber().length()); // Assuming UUID length is 36
    }

    @Test
    public void testEnrichCaseRegistration_Exception() {
        // Arrange
        WitnessRequest witnessRequest = new WitnessRequest();
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid(UUID.randomUUID().toString()); // Mocking UUID
        requestInfo.setUserInfo(userInfo);
        witnessRequest.setRequestInfo(requestInfo);

        witnessRequest.setWitness(null);

        assertThrows(Exception.class, () -> witnessRegistrationEnrichment.enrichWitnessRegistration(witnessRequest));
    }

    @Test
    public void testEnrichWitnessApplicationUponUpdate_Success() {
        // Arrange
        WitnessRequest witnessRequest = new WitnessRequest();
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid(UUID.randomUUID().toString()); // Mocking UUID
        requestInfo.setUserInfo(userInfo);
        witnessRequest.setRequestInfo(requestInfo);

        Witness witness = new Witness();
        witnessRequest.setWitness(witness);

        // Ensure that AuditDetails object is initialized for all witnesses
        if (witness.getAuditDetails() == null) {
            witness.setAuditDetails(new AuditDetails());
        }

        // Act
        witnessRegistrationEnrichment.enrichWitnessApplicationUponUpdate(witnessRequest);

        // Assert
        assertEquals(userInfo.getUuid(), witness.getAuditDetails().getLastModifiedBy());
    }

    @Test
    public void testEnrichWitnessApplicationUponUpdate_Exception() {
        // Arrange
        WitnessRequest witnessRequest = new WitnessRequest();
        RequestInfo requestInfo = new RequestInfo();
        User userInfo = new User();
        userInfo.setUuid(UUID.randomUUID().toString()); // Mocking UUID
        requestInfo.setUserInfo(userInfo);
        witnessRequest.setRequestInfo(requestInfo);

        witnessRequest.setWitness(null);

        // Assert
        assertThrows(Exception.class, () -> witnessRegistrationEnrichment.enrichWitnessApplicationUponUpdate(witnessRequest));
    }

}
