package org.pucar.service;

import org.egov.common.contract.request.RequestInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.kafka.Producer;
import org.pucar.repository.AdvocateClerkRepository;
import org.pucar.repository.querybuilder.AdvocateClerkQueryBuilder;
import org.pucar.web.models.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class AdvocateServiceTest {

    @Mock
    private AdvocateClerkRepository advocateRepository;

    @InjectMocks
    private AdvocateClerkRepository service;

    @InjectMocks
    private AdvocateClerkQueryBuilder queryBuilder;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        queryBuilder = new AdvocateClerkQueryBuilder();
    }
//    @Test
//    public void testSearchAdvocateApplications() {
//        // Setup
//        RequestInfo requestInfo = new RequestInfo();
//        List<AdvocateClerkSearchCriteria> criteriaList = new ArrayList<>();
//        criteriaList.add(new AdvocateClerkSearchCriteria("1", "2", "3"));
//        when(advocateRepository.getApplications(any())).thenReturn(Collections.emptyList());
//
//        // Invoke
//        List<AdvocateClerk> result = service.getApplications(criteriaList);
//
//        // Verify
//        assertEquals(0, result.size());
//        verify(advocateRepository, times(1)).getApplications(criteriaList);
//    }
}
