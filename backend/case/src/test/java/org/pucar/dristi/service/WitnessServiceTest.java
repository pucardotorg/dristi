package org.pucar.dristi.service;
import org.egov.common.contract.request.RequestInfo;
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
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

 class WitnessServiceTest {

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
     void testRegisterWitnessRequest() {
        WitnessRequest request = new WitnessRequest();
        List<Witness> witnesses = new ArrayList<>();
        request.setWitnesses(witnesses);

        doNothing().when(validator).validateCaseRegistration(request);
        doNothing().when(enrichmentUtil).enrichWitnessRegistration(request);
        doNothing().when(producer).push(any(String.class), any(WitnessRequest.class));

        List<Witness> result = witnessService.registerWitnessRequest(request);

        Assert.isTrue(result == witnesses, "Returned witnesses should be same as input");
        verify(validator, times(1)).validateCaseRegistration(request);
        verify(enrichmentUtil, times(1)).enrichWitnessRegistration(request);
        verify(producer, times(1)).push(any(), any(WitnessRequest.class));
    }

    @Test
     void testSearchWitnesses() {
        WitnessSearchRequest searchRequest = new WitnessSearchRequest();

        doReturn(new ArrayList<>()).when(witnessRepository).getApplications(any());

        List<Witness> result = witnessService.searchWitnesses(searchRequest);

        Assert.notNull(result, "Result should not be null");
        Assert.isTrue(result.isEmpty(), "Result should be empty");
        verify(witnessRepository, times(1)).getApplications(any());
    }

    @Test
     void testUpdateWitness() {
        WitnessRequest request = new WitnessRequest();
        List<Witness> witnesses = new ArrayList<>();
        request.setWitnesses(witnesses);

        doReturn(null).when(validator).validateApplicationExistence(any(RequestInfo.class) ,any(Witness.class));
        doNothing().when(enrichmentUtil).enrichWitnessApplicationUponUpdate(request);
        doNothing().when(producer).push(any(String.class), any(WitnessRequest.class));

        List<Witness> result = witnessService.updateWitness(request);

        Assert.isTrue(result.isEmpty(), "Returned witnesses should be empty");
        verify(validator, times(witnesses.size())).validateApplicationExistence(any(RequestInfo.class) ,any(Witness.class));
        verify(enrichmentUtil, times(1)).enrichWitnessApplicationUponUpdate(request);
    }




}
