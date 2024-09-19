package org.pucar.dristi.service;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.common.models.individual.Gender;
import org.egov.common.models.individual.Individual;
import org.egov.common.models.individual.Name;
import org.egov.common.models.individual.Address;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.repository.HearingRepository;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.PdfRequestUtil;
import org.pucar.dristi.web.models.*;
import org.springframework.core.io.ByteArrayResource;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class WitnessDepositionPdfServiceTest {

    @Mock
    private HearingRepository hearingRepository;

    @Mock
    private IndividualService individualService;

    @Mock
    private CaseUtil caseUtil;

    @Mock
    private PdfRequestUtil pdfRequestUtil;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private WitnessDepositionPdfService service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetWitnessDepositionPdf_Success() {
        // Arrange
        HearingSearchRequest searchRequest = createMockHearingSearchRequest();
        Hearing mockHearing = createMockHearing();
        when(hearingRepository.getHearings(any())).thenReturn(Collections.singletonList(mockHearing));

        ObjectNode mockAdditionalDetails = mock(ObjectNode.class);
        when(mapper.convertValue(any(), eq(ObjectNode.class))).thenReturn(mockAdditionalDetails);

        JsonNode mockWitnessDepositions = mock(JsonNode.class);
        when(mockAdditionalDetails.get("witnessDepositions")).thenReturn(mockWitnessDepositions);
        when(mockWitnessDepositions.isEmpty()).thenReturn(false);

        JsonNode mockCaseDetails = mock(JsonNode.class);
        when(caseUtil.searchCaseDetails(any())).thenReturn(mockCaseDetails);

        when(mockCaseDetails.get("filingNumber")).thenReturn(mock(JsonNode.class));
        when(mockCaseDetails.get("filingNumber").asText()).thenReturn("CASE-2023-001");

        List<Individual> mockIndividuals = Collections.singletonList(createMockIndividual());
        when(individualService.getIndividuals(any(), any())).thenReturn(mockIndividuals);

        ByteArrayResource mockPdfResource = mock(ByteArrayResource.class);
        when(pdfRequestUtil.createPdfForWitness(any(), any())).thenReturn(mockPdfResource);

        // Act
        ByteArrayResource result = service.getWitnessDepositionPdf(searchRequest);

        // Assert
        assertNotNull(result);
        verify(hearingRepository).getHearings(any());
        verify(pdfRequestUtil).createPdfForWitness(any(), any());
    }

    @Test
    void testGetWitnessDepositionPdf_NoHearingFound() {
        // Arrange
        HearingSearchRequest searchRequest = createMockHearingSearchRequest();
        when(hearingRepository.getHearings(any())).thenReturn(Collections.emptyList());

        // Act & Assert
        assertThrows(CustomException.class, () -> service.getWitnessDepositionPdf(searchRequest));
    }

    @Test
    void testCreateCaseSearchRequest() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        Hearing hearing = new Hearing();
        hearing.setFilingNumber(Collections.singletonList("CASE-2023-001"));

        // Act
        CaseSearchRequest result = service.createCaseSearchRequest(requestInfo, hearing);

        // Assert
        assertNotNull(result);
        assertEquals(requestInfo, result.getRequestInfo());
        assertEquals(1, result.getCriteria().size());
        assertEquals("CASE-2023-001", result.getCriteria().get(0).getFilingNumber());
    }

    @Test
    void testCreateWitnessObjects() throws Exception {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        Hearing hearing = createMockHearing();

        ObjectNode mockAdditionalDetails = mock(ObjectNode.class);
        when(mapper.convertValue(any(), eq(ObjectNode.class))).thenReturn(mockAdditionalDetails);

        JsonNode mockWitnessDepositions = mock(JsonNode.class);
        when(mockAdditionalDetails.get("witnessDepositions")).thenReturn(mockWitnessDepositions);
        when(mockWitnessDepositions.isEmpty()).thenReturn(false);

        JsonNode mockCaseDetails = mock(JsonNode.class);
        when(caseUtil.searchCaseDetails(any())).thenReturn(mockCaseDetails);

        when(mockCaseDetails.get("filingNumber")).thenReturn(mock(JsonNode.class));
        when(mockCaseDetails.get("filingNumber").asText()).thenReturn("CASE-2023-001");

        List<Individual> mockIndividuals = Collections.singletonList(createMockIndividual());
        when(individualService.getIndividuals(any(), any())).thenReturn(mockIndividuals);

        // Use reflection to access private method
        Method createWitnessObjectsMethod = WitnessDepositionPdfService.class.getDeclaredMethod("createWitnessObjects", RequestInfo.class, Hearing.class);
        createWitnessObjectsMethod.setAccessible(true);

        // Act
        Object result = createWitnessObjectsMethod.invoke(service, requestInfo, hearing);

        // Assert
        assertNotNull(result);
    }

    @Test
    void testExtractCaseNumber() throws Exception {
        Method extractCaseNumberMethod = WitnessDepositionPdfService.class.getDeclaredMethod("extractCaseNumber", String.class);
        extractCaseNumberMethod.setAccessible(true);

        assertEquals("1", extractCaseNumberMethod.invoke(service, "CASE-2023-001"));
        assertEquals("", extractCaseNumberMethod.invoke(service, "INVALID-FORMAT"));
        assertEquals("", extractCaseNumberMethod.invoke(service, (Object) null));
    }

    @Test
    void testExtractCaseYear() {
        assertEquals("2023", WitnessDepositionPdfService.extractCaseYear("CASE-2023-001"));
        assertEquals("", WitnessDepositionPdfService.extractCaseYear("INVALID-FORMAT"));
        assertEquals("", WitnessDepositionPdfService.extractCaseYear(null));
    }

    @Test
    void testCalculateAge() throws Exception {
        Method calculateAgeMethod = WitnessDepositionPdfService.class.getDeclaredMethod("calculateAge", Date.class);
        calculateAgeMethod.setAccessible(true);

        Date birthDate = Date.from(LocalDate.now().minusYears(30).atStartOfDay(ZoneId.systemDefault()).toInstant());
        assertEquals(30, calculateAgeMethod.invoke(service, birthDate));
        assertNull(calculateAgeMethod.invoke(service, (Date) null));
    }

    @Test
    void testFormatDateFromMillis() throws Exception {
        Method formatDateFromMillisMethod = WitnessDepositionPdfService.class.getDeclaredMethod("formatDateFromMillis", long.class);
        formatDateFromMillisMethod.setAccessible(true);

        long millis = System.currentTimeMillis();
        String result = (String) formatDateFromMillisMethod.invoke(service, millis);
        assertNotNull(result);
        assertFalse(result.isEmpty());

        // Test error case
        assertEquals("1 day of January 1970", formatDateFromMillisMethod.invoke(service, -1L));
    }

    @Test
    void testSafeGetText() throws Exception {
        Method safeGetTextMethod = WitnessDepositionPdfService.class.getDeclaredMethod("safeGetText", JsonNode.class, String.class, String.class);
        safeGetTextMethod.setAccessible(true);

        ObjectNode node = new ObjectMapper().createObjectNode();
        node.put("key", "value");

        assertEquals("value", safeGetTextMethod.invoke(service, node, "key", "default"));
        assertEquals("default", safeGetTextMethod.invoke(service, node, "nonexistent", "default"));
        assertEquals("default", safeGetTextMethod.invoke(service, null, "key", "default"));
    }

    @Test
    void testCreateWitnessObjects_FullFlow() throws Exception {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        Hearing hearing = createMockHearing();

        ObjectNode mockAdditionalDetails = mock(ObjectNode.class);
        when(mapper.convertValue(any(), eq(ObjectNode.class))).thenReturn(mockAdditionalDetails);

        JsonNode mockWitnessDepositions = mock(JsonNode.class);
        when(mockAdditionalDetails.has("witnessDepositions")).thenReturn(true);
        when(mockAdditionalDetails.get("witnessDepositions")).thenReturn(mockWitnessDepositions);
        when(mockWitnessDepositions.isEmpty()).thenReturn(false);

        // Mock witness data
        JsonNode mockWitnessNode = mock(JsonNode.class);
        when(mockWitnessDepositions.iterator()).thenReturn(Collections.singletonList(mockWitnessNode).iterator());
        when(mockWitnessNode.path("uuid")).thenReturn(mock(JsonNode.class));
        when(mockWitnessNode.path("uuid").asText()).thenReturn("test-uuid");

        // Mock case details
        JsonNode mockCaseDetails = mock(JsonNode.class);
        when(caseUtil.searchCaseDetails(any())).thenReturn(mockCaseDetails);
        when(mockCaseDetails.get("filingNumber")).thenReturn(mock(JsonNode.class));
        when(mockCaseDetails.get("filingNumber").asText()).thenReturn("CASE-2023-001");

        // Mock individual service
        List<Individual> mockIndividuals = Collections.singletonList(createMockIndividual());
        when(individualService.getIndividuals(any(), any())).thenReturn(mockIndividuals);

        // Use reflection to access private method
        Method createWitnessObjectsMethod = WitnessDepositionPdfService.class.getDeclaredMethod("createWitnessObjects", RequestInfo.class, Hearing.class);
        createWitnessObjectsMethod.setAccessible(true);

        // Act
       Object result = createWitnessObjectsMethod.invoke(service, requestInfo, hearing);

        // Assert
        assertNotNull(result);
        assertInstanceOf(List.class, result);

        // Verify method calls
        verify(caseUtil).searchCaseDetails(any());
        verify(mockCaseDetails).get("filingNumber");
        verify(individualService).getIndividuals(any(), any());
    }


    private HearingSearchRequest createMockHearingSearchRequest() {
        return HearingSearchRequest.builder()
                .criteria(HearingCriteria.builder().build())
                .requestInfo(RequestInfo.builder().userInfo(User.builder().tenantId("kl").build()).build())
                .build();
    }

    private Hearing createMockHearing() {
        return Hearing.builder()
                .hearingId(UUID.randomUUID().toString())
                .filingNumber(Collections.singletonList("CASE-2023-001"))
                .endTime(System.currentTimeMillis())
                .build();
    }

    private Individual createMockIndividual() {
        return Individual.builder()
                .userUuid(UUID.randomUUID().toString())
                .name(Name.builder().givenName("John").familyName("Doe").build())
                .mobileNumber("1234567890")
                .fatherName("Father Name")
                .dateOfBirth(new Date())
                .gender(Gender.MALE)
                .address(Collections.singletonList(Address.builder()
                        .city("City")
                        .addressLine1("Address Line 1")
                        .build()))
                .build();
    }
}