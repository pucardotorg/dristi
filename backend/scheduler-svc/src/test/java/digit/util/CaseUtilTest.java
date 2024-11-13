package digit.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import digit.config.Configuration;
import digit.repository.ServiceRequestRepository;
import digit.web.models.cases.SearchCaseRequest;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CaseUtilTest {

    @Mock
    private Configuration mockConfig;

    @Mock
    private ObjectMapper mockMapper;

    @Mock
    private ServiceRequestRepository mockRequestRepository;

    @Mock
    private SearchCaseRequest searchCaseRequest;

    @InjectMocks
    private CaseUtil caseUtil;

    @Mock
    private CaseUtil caseUtilMock;


    @Test
    public void testGetCases_Success() throws Exception {
        when(mockConfig.getCaseUrl()).thenReturn("http://example.com");
        when(mockConfig.getCaseEndpoint()).thenReturn("/cases");

        JsonNode mockResponseListNode = mock(JsonNode.class);
        JsonNode mockCriteriaNode = mock(JsonNode.class);
        JsonNode mockJsonNode = mock(JsonNode.class);

        when(mockJsonNode.get("criteria")).thenReturn(mockCriteriaNode);
        when(mockCriteriaNode.get(0)).thenReturn(mockResponseListNode);
        when(mockResponseListNode.get("responseList")).thenReturn(mock(JsonNode.class)); // or whatever structure you expect

        when(mockMapper.readTree(anyString())).thenReturn(mockJsonNode);

        when(mockRequestRepository.postMethod(any(StringBuilder.class), any(SearchCaseRequest.class)))
                .thenReturn("{\"criteria\":[{\"responseList\":[]}]}");

        JsonNode result = caseUtil.getCases(new SearchCaseRequest());

        assertNotNull(result);
    }

    @Test
    public void testGetCases_JsonProcessingException() throws JsonProcessingException {
        when(mockConfig.getCaseUrl()).thenReturn("http://example.com");
        when(mockConfig.getCaseEndpoint()).thenReturn("/cases");
        when(mockRequestRepository.postMethod(any(StringBuilder.class), any(SearchCaseRequest.class)))
                .thenReturn("{\"criteria\":[{\"responseList\":[]}]}");

            when(mockMapper.readTree(anyString())).thenThrow(new CustomException("DK_RR_JSON_PROCESSING_ERR", "Invalid Json response"));

        assertThrows(CustomException.class, () -> caseUtil.getCases(new SearchCaseRequest()));
    }

    @Test
    public void testGetIdsFromJsonNodeArray() {
        JsonNode mockNode1 = mock(JsonNode.class);
        when(mockNode1.get("id")).thenReturn(mock(JsonNode.class));
        when(mockNode1.get("id").asText()).thenReturn("1");

        JsonNode mockNode2 = mock(JsonNode.class);
        when(mockNode2.get("id")).thenReturn(null); // Simulate case where 'id' is missing

        JsonNode[] mockArray = {mockNode1, mockNode2};
        JsonNode mockNodeArray = mock(JsonNode.class);
        when(mockNodeArray.isArray()).thenReturn(true);
        when(mockNodeArray.iterator()).thenReturn(new ArrayIterator<>(mockArray));

        Set<String> result = caseUtil.getIdsFromJsonNodeArray(mockNodeArray);

        assertEquals(1, result.size());
        assertTrue(result.contains("1"));
    }

    @Test
    public void testGetLitigantsFromRepresentatives_Success() {
        Set<String> litigants = new java.util.HashSet<>(Set.of("1", "2"));

        JsonNode mockRepresentativesNode = mock(JsonNode.class);
        JsonNode mockRepresentingNode = mock(JsonNode.class);
        JsonNode mockIndividualIdNode = mock(JsonNode.class);

        when(mockRepresentativesNode.isArray()).thenReturn(true);
        when(mockRepresentativesNode.iterator()).thenReturn(new ArrayIterator<>(new JsonNode[]{mockRepresentingNode}));
        when(mockRepresentingNode.get("representing")).thenReturn(mock(JsonNode.class));
        when(mockRepresentingNode.get("representing").iterator()).thenReturn(new ArrayIterator<>(new JsonNode[]{mockIndividualIdNode}));
        when(mockIndividualIdNode.get("individualId")).thenReturn(mock(JsonNode.class));
        when(mockIndividualIdNode.get("individualId").asText()).thenReturn("1");

        Set<String> result = caseUtil.getLitigantsFromRepresentatives(litigants, mockRepresentativesNode);

        assertEquals(1, result.size());
        assertFalse(result.contains("1"));
    }

    @Test
    public void testGetRepresentatives_WithCaseRes_Success() {
        JsonNode mockCaseRes = mock(JsonNode.class);
        JsonNode mockRepresentativesNode = mock(JsonNode.class);

        when(mockCaseRes.isArray()).thenReturn(true);
        when(mockCaseRes.isEmpty()).thenReturn(false);
        when(mockCaseRes.get(0)).thenReturn(mock(JsonNode.class));
        when(mockCaseRes.get(0).get("representatives")).thenReturn(mockRepresentativesNode);

        JsonNode result = caseUtil.getRepresentatives(mockCaseRes);

        assertNotNull(result);
        assertEquals(mockRepresentativesNode, result);
    }

    @Test
    public void testGetRepresentatives_WithCaseRes_Failure() {
        JsonNode mockCaseRes = mock(JsonNode.class);

        when(mockCaseRes.isArray()).thenReturn(true);
        when(mockCaseRes.isEmpty()).thenReturn(true); // Simulate empty case list

        assertThrows(CustomException.class, () -> caseUtil.getRepresentatives(mockCaseRes));
    }



    @Test
    public void testGetLitigants_Success() throws Exception {
        JsonNode mockCaseList = mock(JsonNode.class);
        JsonNode mockLitigantsNode = mock(JsonNode.class);
        JsonNode mockCriteriaNode = mock(JsonNode.class);
        JsonNode mockResponseListNode = mock(JsonNode.class);

        when(mockConfig.getCaseUrl()).thenReturn("http://example.com");
        when(mockConfig.getCaseEndpoint()).thenReturn("/cases");

        when(mockRequestRepository.postMethod(any(StringBuilder.class), any(SearchCaseRequest.class)))
                .thenReturn("{\"criteria\":[{\"responseList\":[]}]}");

        when(mockMapper.readTree(anyString())).thenReturn(mockCaseList);
        when(mockCaseList.get("criteria")).thenReturn(mockCriteriaNode);
        when(mockCriteriaNode.get(0)).thenReturn(mockResponseListNode);
        when(mockResponseListNode.get("responseList")).thenReturn(mockLitigantsNode);

        when(mockLitigantsNode.isArray()).thenReturn(true);
        when(mockLitigantsNode.get(0)).thenReturn(mockLitigantsNode);
        when(mockLitigantsNode.get("litigants")).thenReturn(mockLitigantsNode);

        JsonNode result = caseUtil.getLitigants(new SearchCaseRequest());

        assertNotNull(result);
        assertEquals(mockLitigantsNode, result);
    }

    @Test
    void testGetLitigants_Failure() throws Exception {
        JsonNode mockCaseList = mock(JsonNode.class);
        JsonNode mockLitigantsNode = mock(JsonNode.class);
        JsonNode jsonNode = mock(JsonNode.class);

        when(mockConfig.getCaseUrl()).thenReturn("http://example.com");
        when(mockConfig.getCaseEndpoint()).thenReturn("/cases");
        when(mockRequestRepository.postMethod(any(StringBuilder.class), any(SearchCaseRequest.class)))
                .thenReturn("{\"criteria\":[{\"responseList\":[]}]}");
        when(mockMapper.readTree(anyString())).thenReturn(mockLitigantsNode);
        when(mockLitigantsNode.get("criteria")).thenReturn(jsonNode);
        when(jsonNode.get(0)).thenReturn(mockLitigantsNode);

        JsonNode temp = mock(JsonNode.class);
        when(mockCaseList.get(0)).thenReturn(mockLitigantsNode);
        when(mockCaseList.get(0).get("responseList")).thenReturn(mockLitigantsNode);

        assertThrows(CustomException.class, () -> caseUtil.getLitigants(new SearchCaseRequest()));
    }

    @Test
    void testGetLitigants_WithCaseList_Success() {
        JsonNode mockCaseList = mock(JsonNode.class);
        JsonNode mockLitigantsNode = mock(JsonNode.class);

        when(mockCaseList.isArray()).thenReturn(true);
        when(mockCaseList.isEmpty()).thenReturn(false);
        when(mockCaseList.get(0)).thenReturn(mock(JsonNode.class));
        when(mockCaseList.get(0).get("litigants")).thenReturn(mockLitigantsNode);

        JsonNode result = caseUtil.getLitigants(mockCaseList);

        assertNotNull(result);
        assertEquals(mockLitigantsNode, result);
    }

    @Test
    void testGetLitigants_WithCaseList_Failure() {
        JsonNode mockCaseList = mock(JsonNode.class);

        when(mockCaseList.isArray()).thenReturn(true);
        when(mockCaseList.isEmpty()).thenReturn(true); // Simulate empty case list

        assertThrows(CustomException.class, () -> caseUtil.getLitigants(mockCaseList));
    }

    @Test
    void testGetRepresentatives_Success() throws Exception {
        JsonNode mockCaseList = mock(JsonNode.class);
        JsonNode mockRepresentativesNode = mock(JsonNode.class);
        JsonNode mockCriteriaNode = mock(JsonNode.class);
        JsonNode mockResponseListNode = mock(JsonNode.class);

        when(mockConfig.getCaseUrl()).thenReturn("http://example.com");
        when(mockConfig.getCaseEndpoint()).thenReturn("/cases");

        when(mockRequestRepository.postMethod(any(StringBuilder.class), any(SearchCaseRequest.class)))
                .thenReturn("{\"criteria\":[{\"responseList\":[]}]}");

        when(mockMapper.readTree(anyString())).thenReturn(mockCaseList);
        when(mockCaseList.get("criteria")).thenReturn(mockCriteriaNode);
        when(mockCriteriaNode.get(0)).thenReturn(mockResponseListNode);
        when(mockResponseListNode.get("responseList")).thenReturn(mockRepresentativesNode);

        when(mockRepresentativesNode.isArray()).thenReturn(true);
        when(mockRepresentativesNode.get(0)).thenReturn(mockRepresentativesNode);
        when(mockRepresentativesNode.get("representatives")).thenReturn(mockRepresentativesNode);

        JsonNode result = caseUtil.getRepresentatives(new SearchCaseRequest());

        assertNotNull(result);
        assertEquals(mockRepresentativesNode, result);
    }

    @Test
    void testGetRepresentatives_Failure() throws Exception {
        JsonNode mockCaseList = mock(JsonNode.class);
        JsonNode mockRepresentativesNode = mock(JsonNode.class);
        JsonNode jsonNode = mock(JsonNode.class);

        when(mockConfig.getCaseUrl()).thenReturn("http://example.com");
        when(mockConfig.getCaseEndpoint()).thenReturn("/cases");
        when(mockRequestRepository.postMethod(any(StringBuilder.class), any(SearchCaseRequest.class)))
                .thenReturn("{\"criteria\":[{\"responseList\":[]}]}");
        when(mockMapper.readTree(anyString())).thenReturn(mockRepresentativesNode);
        when(mockRepresentativesNode.get("criteria")).thenReturn(jsonNode);
        when(jsonNode.get(0)).thenReturn(mockRepresentativesNode);

        JsonNode temp = mock(JsonNode.class);
        when(mockCaseList.get(0)).thenReturn(mockRepresentativesNode);
        when(mockCaseList.get(0).get("responseList")).thenReturn(mockRepresentativesNode);

        assertThrows(CustomException.class, () -> caseUtil.getRepresentatives(new SearchCaseRequest()));
    }

    @Test
    void testGetRepresentatives_WithCaseList_Success() {
        JsonNode mockCaseList = mock(JsonNode.class);
        JsonNode mockRepresentativesNode = mock(JsonNode.class);

        when(mockCaseList.isArray()).thenReturn(true);
        when(mockCaseList.isEmpty()).thenReturn(false);
        when(mockCaseList.get(0)).thenReturn(mock(JsonNode.class));
        when(mockCaseList.get(0).get("representatives")).thenReturn(mockRepresentativesNode);

        JsonNode result = caseUtil.getRepresentatives(mockCaseList);

        assertNotNull(result);
        assertEquals(mockRepresentativesNode, result);
    }

    @Test
    void testGetRepresentatives_WithCaseList_Failure() {
        JsonNode mockCaseList = mock(JsonNode.class);

        when(mockCaseList.isArray()).thenReturn(true);
        when(mockCaseList.isEmpty()).thenReturn(true); // Simulate empty case list

        assertThrows(CustomException.class, () -> caseUtil.getRepresentatives(mockCaseList));
    }

    @Test
    void testGetLinkedCases_Success() throws Exception {
        JsonNode mockCaseList = mock(JsonNode.class);
        JsonNode mockLinkedCasesNode = mock(JsonNode.class);
        JsonNode mockCriteriaNode = mock(JsonNode.class);
        JsonNode mockResponseListNode = mock(JsonNode.class);

        when(mockConfig.getCaseUrl()).thenReturn("http://example.com");
        when(mockConfig.getCaseEndpoint()).thenReturn("/cases");

        when(mockRequestRepository.postMethod(any(StringBuilder.class), any(SearchCaseRequest.class)))
                .thenReturn("{\"criteria\":[{\"responseList\":[]}]}");

        when(mockMapper.readTree(anyString())).thenReturn(mockCaseList);
        when(mockCaseList.get("criteria")).thenReturn(mockCriteriaNode);
        when(mockCriteriaNode.get(0)).thenReturn(mockResponseListNode);
        when(mockResponseListNode.get("responseList")).thenReturn(mockLinkedCasesNode);

        when(mockLinkedCasesNode.isArray()).thenReturn(true);
        when(mockLinkedCasesNode.get(0)).thenReturn(mockLinkedCasesNode);
        when(mockLinkedCasesNode.get("linkedCases")).thenReturn(mockLinkedCasesNode);

        JsonNode result = caseUtil.getLinkedCases(new SearchCaseRequest());

        assertNotNull(result);
        assertEquals(mockLinkedCasesNode, result);
    }

    @Test
    void testGetLinkedCases_Failure() throws Exception {
        JsonNode mockCaseList = mock(JsonNode.class);
        JsonNode mockLinkedCasesNode = mock(JsonNode.class);
        JsonNode jsonNode = mock(JsonNode.class);

        when(mockConfig.getCaseUrl()).thenReturn("http://example.com");
        when(mockConfig.getCaseEndpoint()).thenReturn("/cases");
        when(mockRequestRepository.postMethod(any(StringBuilder.class), any(SearchCaseRequest.class)))
                .thenReturn("{\"criteria\":[{\"responseList\":[]}]}");
        when(mockMapper.readTree(anyString())).thenReturn(mockLinkedCasesNode);
        when(mockLinkedCasesNode.get("criteria")).thenReturn(jsonNode);
        when(jsonNode.get(0)).thenReturn(mockLinkedCasesNode);

        JsonNode temp = mock(JsonNode.class);
        when(mockCaseList.get(0)).thenReturn(mockLinkedCasesNode);
        when(mockCaseList.get(0).get("responseList")).thenReturn(mockLinkedCasesNode);

        assertThrows(CustomException.class, () -> caseUtil.getLinkedCases(new SearchCaseRequest()));
    }

    @Test
    void testGetIndividualIds_Success() throws Exception {
        JsonNode mockIndividualsNode = mock(JsonNode.class);
        JsonNode mockIndividualIdNode = mock(JsonNode.class);

        when(mockIndividualsNode.isArray()).thenReturn(true);
        when(mockIndividualsNode.iterator()).thenReturn(new ArrayIterator<>(new JsonNode[]{mockIndividualIdNode}));
        when(mockIndividualIdNode.get("individualId")).thenReturn(mock(JsonNode.class));
        when(mockIndividualIdNode.get("individualId").asText()).thenReturn("1");

        Set<String> result = caseUtil.getIndividualIds(mockIndividualsNode);

        assertEquals(1, result.size());
        assertTrue(result.contains("1"));
    }

    static class ArrayIterator<T> implements java.util.Iterator<T> {
        private final T[] array;
        private int index = 0;

        ArrayIterator(T[] array) {
            this.array = array;
        }

        @Override
        public boolean hasNext() {
            return index < array.length;
        }

        @Override
        public T next() {
            return array[index++];
        }
    }
}
