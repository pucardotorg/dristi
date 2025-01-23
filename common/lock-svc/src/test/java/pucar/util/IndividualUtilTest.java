package pucar.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pucar.config.Configuration;
import pucar.repository.ServiceRequestRepository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IndividualUtilTest {

    @Mock
    private ServiceRequestRepository serviceRequestRepository;

    @Mock
    private Configuration config;

    @InjectMocks
    private IndividualUtil individualUtil;


    @Test
    void testGetIndividualId_Success() {
        RequestInfo requestInfo = createMockRequestInfo();
        JsonObject mockIndividualResponse = createMockIndividualResponse();

        when(config.getIndividualHost()).thenReturn("http://test-service");
        when(config.getIndividualSearchEndpoint()).thenReturn("/individual/search");
        when(serviceRequestRepository.fetchResult(any(), any())).thenReturn(mockIndividualResponse);

        String individualId = individualUtil.getIndividualId(requestInfo);

        assertNotNull(individualId);
        assertEquals("individual123", individualId);
    }

    @Test
    void testGetIndividualId_Exception() {
        RequestInfo requestInfo = createMockRequestInfo();

        when(config.getIndividualHost()).thenReturn("http://test-service");
        when(serviceRequestRepository.fetchResult(any(), any())).thenThrow(new RuntimeException("Service failure"));

        CustomException exception = assertThrows(CustomException.class, () -> individualUtil.getIndividualId(requestInfo));

        assertEquals("Exception in individual utility service: Service failure", exception.getMessage());
    }

    private RequestInfo createMockRequestInfo() {
        User user = User.builder()
                .uuid("user123")
                .tenantId("tenant123")
                .build();

        return RequestInfo.builder()
                .userInfo(user)
                .build();
    }

    private JsonObject createMockIndividualResponse() {
        JsonObject individual = new JsonObject();
        individual.addProperty("individualId", "individual123");

        JsonArray individualArray = new JsonArray();
        individualArray.add(individual);

        JsonObject response = new JsonObject();
        response.add("Individual", individualArray);

        return response;
    }
}
