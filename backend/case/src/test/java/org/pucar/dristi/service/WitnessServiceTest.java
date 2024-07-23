package org.pucar.dristi.service;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.enrichment.WitnessRegistrationEnrichment;
import org.pucar.dristi.kafka.Producer;
import org.pucar.dristi.repository.WitnessRepository;
import org.pucar.dristi.validators.WitnessRegistrationValidator;
import org.pucar.dristi.web.models.Witness;
import org.pucar.dristi.web.models.WitnessRequest;
import org.pucar.dristi.web.models.WitnessSearchRequest;
import org.springframework.util.Assert;

public class WitnessServiceTest {

    @Mock
    private WitnessRegistrationValidator validator;

    @Mock
    private WitnessRegistrationEnrichment enrichmentUtil;

    @Mock
    private WitnessRepository witnessRepository;

    @Mock
    private Producer producer;
    @Mock
    private Configuration config;
    @InjectMocks
    private WitnessService witnessService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testRegisterWitnessRequest() {
        WitnessRequest request = new WitnessRequest();
        request.setWitness(new Witness());

        doNothing().when(validator).validateCaseRegistration(request);
        doNothing().when(enrichmentUtil).enrichWitnessRegistration(request);
        doNothing().when(producer).push(any(String.class), any(WitnessRequest.class));

        Witness result = witnessService.registerWitnessRequest(request);

        Assert.isTrue(result == request.getWitness(), "Returned witnesses should be same as input");
        verify(validator, times(1)).validateCaseRegistration(request);
        verify(enrichmentUtil, times(1)).enrichWitnessRegistration(request);
        verify(producer, times(1)).push(any(), any(WitnessRequest.class));
    }

    @Test
    public void testRegisterWitnessRequest_CustomException() {
        WitnessRequest request = new WitnessRequest();
        request.setWitness(new Witness());

        doThrow(new CustomException()).when(validator).validateCaseRegistration(any());

        assertThrows(Exception.class, () -> {
            witnessService.registerWitnessRequest(request);
        });
    }

    @Test
    public void testRegisterWitnessRequest_Exception() {
        WitnessRequest request = null;

        assertThrows(Exception.class, () -> {
            witnessService.registerWitnessRequest(request);
        });
    }

    @Test
    public void testSearchWitnesses() {
        WitnessSearchRequest searchRequest = new WitnessSearchRequest();

        doReturn(new ArrayList<>()).when(witnessRepository).getApplications(any());

        List<Witness> result = witnessService.searchWitnesses(searchRequest);

        Assert.notNull(result, "Result should not be null");
        Assert.isTrue(result.isEmpty(), "Result should be empty");
        verify(witnessRepository, times(1)).getApplications(any());
    }

    @Test
    public void testSearchWitnesses_Exception() {
        WitnessSearchRequest searchRequest = new WitnessSearchRequest();
        when(witnessRepository.getApplications((any()))).thenThrow(new CustomException());

        assertThrows(CustomException.class, () -> {
            witnessService.searchWitnesses(searchRequest);
        });
    }

    @Test
    public void testSearchWitnesses_CustomException() {
        WitnessSearchRequest searchRequest = new WitnessSearchRequest();
        when(witnessRepository.getApplications((any()))).thenThrow(new RuntimeException());

        assertThrows(Exception.class, () -> {
            witnessService.searchWitnesses(searchRequest);
        });
    }

    @Test
    public void testUpdateWitness() {
        WitnessRequest request = new WitnessRequest();
        request.setRequestInfo(new RequestInfo());
        request.setWitness(new Witness());

        doReturn(null).when(validator).validateApplicationExistence(any(RequestInfo.class) ,any(Witness.class));
        doNothing().when(enrichmentUtil).enrichWitnessApplicationUponUpdate(request);
        doNothing().when(producer).push(any(String.class), any(WitnessRequest.class));

        witnessService.updateWitness(request);

        verify(validator, times(1)).validateApplicationExistence(any(RequestInfo.class) ,any(Witness.class));
        verify(enrichmentUtil, times(1)).enrichWitnessApplicationUponUpdate(request);
    }

    @Test
    public void testUpdateWitness_CustomException() {
        WitnessRequest request = new WitnessRequest();
        request.setRequestInfo(new RequestInfo());
        request.setWitness(null);

        when(validator.validateApplicationExistence(any(), any())).thenThrow(new CustomException());

        assertThrows(CustomException.class, () -> {
            witnessService.updateWitness(request);
        });
    }

    @Test
    public void testUpdateWitness_Exception() {
        WitnessRequest request = new WitnessRequest();
        request.setRequestInfo(new RequestInfo());
        request.setWitness(new Witness());

        doReturn(null).when(validator).validateApplicationExistence(any(RequestInfo.class) ,any(Witness.class));
        doThrow(new RuntimeException()).when(enrichmentUtil).enrichWitnessApplicationUponUpdate(request);

        assertThrows(Exception.class, () -> {
            witnessService.updateWitness(request);
        });
    }




}
