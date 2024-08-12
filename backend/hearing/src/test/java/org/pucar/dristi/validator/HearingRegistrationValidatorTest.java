package org.pucar.dristi.validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONArray;
import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.RequestInfo;
import org.egov.common.contract.request.User;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.pucar.dristi.config.Configuration;
import org.pucar.dristi.repository.HearingRepository;
import org.pucar.dristi.service.IndividualService;
import org.pucar.dristi.util.ApplicationUtil;
import org.pucar.dristi.util.CaseUtil;
import org.pucar.dristi.util.FileStoreUtil;
import org.pucar.dristi.util.MdmsUtil;
import org.pucar.dristi.web.models.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.INVALID_FILESTORE_ID;

@Slf4j
class HearingRegistrationValidatorTest {

    @Mock
    private IndividualService individualService;

    @Mock
    private HearingRepository repository;

    @Mock
    private MdmsUtil mdmsUtil;

    @Mock
    private FileStoreUtil fileStoreUtil;

    @Mock
    private Configuration config;

    @Mock
    private ObjectMapper mapper;

    @Mock
    private CaseUtil caseUtil;

    @Mock
    private ApplicationUtil applicationUtil;

    @InjectMocks
    private HearingRegistrationValidator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testValidateHearingRegistration_Success() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(new User());
        requestInfo.getUserInfo().setTenantId("tenant1");

        Hearing hearing = new Hearing();
        hearing.setTenantId("tenant1");
        hearing.setHearingType("type1");
        hearing.setAttendees(Collections.singletonList( Attendee.builder().individualId("Attendee1").build()));
        hearing.setCnrNumbers(Collections.singletonList("cnr1"));
        hearing.setFilingNumber(Collections.singletonList("filing1"));
        hearing.setApplicationNumbers(Collections.singletonList("app1"));

        HearingRequest hearingRequest = new HearingRequest();
        hearingRequest.setRequestInfo(requestInfo);
        hearingRequest.setHearing(hearing);

        JSONArray hearingTypeList = new JSONArray();
        hearingTypeList.add(Map.of("type", "type1"));

        Map<String, Map<String, JSONArray>> mdmsData = Map.of(
                "module1", Map.of("master1", hearingTypeList)
        );

        CaseExistsResponse caseExistsResponse = new CaseExistsResponse();
        caseExistsResponse.setCriteria(Collections.singletonList(CaseExists.builder().cnrNumber("cnr1").exists(true).build()));

        ApplicationExistsResponse applicationExistsResponse = new ApplicationExistsResponse();
        applicationExistsResponse.setApplicationExists(Collections.singletonList(ApplicationExists.builder().applicationNumber("app1").exists(true).build()));

        when(mdmsUtil.fetchMdmsData(any(), any(), any(), any())).thenReturn(mdmsData);
        when(config.getMdmsHearingModuleName()).thenReturn("module1");
        when(config.getMdmsHearingTypeMasterName()).thenReturn("master1");
        when(mapper.convertValue(any(), eq(HearingType.class))).thenReturn(HearingType.builder().type("type1").build());
        when(individualService.searchIndividual(any(), anyString(), anyMap())).thenReturn(true);
        when(caseUtil.fetchCaseDetails(any())).thenReturn(caseExistsResponse);
        when(applicationUtil.fetchApplicationDetails(any())).thenReturn(applicationExistsResponse);
        when(config.getVerifyAttendeeIndividualId()).thenReturn(true);

        // Act
        assertDoesNotThrow(() -> validator.validateHearingRegistration(hearingRequest));
    }

    @Test
    void testValidateHearingRegistration_MissingUserInfo() {
        // Arrange
        HearingRequest hearingRequest = new HearingRequest();
        hearingRequest.setRequestInfo(new RequestInfo());
        hearingRequest.setHearing(new Hearing());

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> validator.validateHearingRegistration(hearingRequest));
        assertEquals("User info not found!!!", exception.getMessage());
    }

    @Test
    void testValidateHearingRegistration_InvalidIndividualId() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(new User());
        requestInfo.getUserInfo().setTenantId("tenant1");

        Hearing hearing = new Hearing();
        hearing.setTenantId("tenant1");
        hearing.setHearingType("type1");
        hearing.setAttendees(Collections.singletonList( Attendee.builder().individualId("individual1").build()));

        HearingRequest hearingRequest = new HearingRequest();
        hearingRequest.setRequestInfo(requestInfo);
        hearingRequest.setHearing(hearing);

        when(individualService.searchIndividual(any(), anyString(), anyMap())).thenReturn(false);
        when(config.getVerifyAttendeeIndividualId()).thenReturn(true);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> validator.validateHearingRegistration(hearingRequest));
        assertEquals("Requested Individual not found or does not exist. ID: individual1", exception.getMessage());
    }

    @Test
    void testValidateHearingRegistration_InvalidHearingType() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(new User());
        requestInfo.getUserInfo().setTenantId("tenant1");

        Hearing hearing = new Hearing();
        hearing.setTenantId("tenant1");
        hearing.setHearingType("type1");

        HearingRequest hearingRequest = new HearingRequest();
        hearingRequest.setRequestInfo(requestInfo);
        hearingRequest.setHearing(hearing);

        JSONArray hearingTypeList = new JSONArray();

        Map<String, Map<String, JSONArray>> mdmsData = Map.of(
                "module1", Map.of("master1", hearingTypeList)
        );

        when(mdmsUtil.fetchMdmsData(any(), any(), any(), any())).thenReturn(mdmsData);
        when(config.getMdmsHearingModuleName()).thenReturn("module1");
        when(config.getMdmsHearingTypeMasterName()).thenReturn("master1");

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> validator.validateHearingRegistration(hearingRequest));
        assertEquals("Could not validate Hearing Type!!!", exception.getMessage());
    }

    @Test
    void testValidateHearingRegistration_NonExistingCaseNumbers() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(new User());
        requestInfo.getUserInfo().setTenantId("tenant1");

        Hearing hearing = new Hearing();
        hearing.setTenantId("tenant1");
        hearing.setHearingType("type1");
        hearing.setCnrNumbers(Collections.singletonList("cnr1"));

        HearingRequest hearingRequest = new HearingRequest();
        hearingRequest.setRequestInfo(requestInfo);
        hearingRequest.setHearing(hearing);

        JSONArray hearingTypeList = new JSONArray();
        hearingTypeList.add(Map.of("type", "type1"));

        Map<String, Map<String, JSONArray>> mdmsData = Map.of(
                "module1", Map.of("master1", hearingTypeList)
        );

        CaseExistsResponse caseExistsResponse = new CaseExistsResponse();
        caseExistsResponse.setCriteria(Collections.singletonList(CaseExists.builder().cnrNumber("cnr1").exists(false).build()));

        when(mdmsUtil.fetchMdmsData(any(), any(), any(), any())).thenReturn(mdmsData);
        when(config.getMdmsHearingModuleName()).thenReturn("module1");
        when(config.getMdmsHearingTypeMasterName()).thenReturn("master1");
        when(mapper.convertValue(any(), eq(HearingType.class))).thenReturn(HearingType.builder().type("type1").build());
        when(individualService.searchIndividual(any(), anyString(), anyMap())).thenReturn(true);
        when(caseUtil.fetchCaseDetails(any())).thenReturn(caseExistsResponse);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> validator.validateHearingRegistration(hearingRequest));
        assertEquals("Cnr Number: cnr1 does not exist ", exception.getMessage());
    }

    @Test
    void testValidateHearingRegistration_NonExistingApplicationNumbers() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setUserInfo(new User());
        requestInfo.getUserInfo().setTenantId("tenant1");

        Hearing hearing = new Hearing();
        hearing.setTenantId("tenant1");
        hearing.setHearingType("type1");
        hearing.setApplicationNumbers(Collections.singletonList("app1"));

        HearingRequest hearingRequest = new HearingRequest();
        hearingRequest.setRequestInfo(requestInfo);
        hearingRequest.setHearing(hearing);

        JSONArray hearingTypeList = new JSONArray();
        hearingTypeList.add(Map.of("type", "type1"));

        Map<String, Map<String, JSONArray>> mdmsData = Map.of(
                "module1", Map.of("master1", hearingTypeList)
        );
        CaseExistsResponse caseExistsResponse = new CaseExistsResponse();
        caseExistsResponse.setCriteria(Collections.singletonList(CaseExists.builder().cnrNumber("cnr1").exists(true).build()));


        ApplicationExistsResponse applicationExistsResponse = new ApplicationExistsResponse();
        applicationExistsResponse.setApplicationExists(Collections.singletonList(ApplicationExists.builder().applicationNumber("app1").exists(false).build()));

        when(mdmsUtil.fetchMdmsData(any(), anyString(), anyString(), anyList())).thenReturn(mdmsData);
        when(config.getMdmsHearingModuleName()).thenReturn("module1");
        when(config.getMdmsHearingTypeMasterName()).thenReturn("master1");
        when(mapper.convertValue(any(), eq(HearingType.class))).thenReturn(HearingType.builder().type("type1").build());
        when(individualService.searchIndividual(any(), anyString(), anyMap())).thenReturn(true);
        when(caseUtil.fetchCaseDetails(any())).thenReturn(caseExistsResponse);
        when(applicationUtil.fetchApplicationDetails(any())).thenReturn(applicationExistsResponse);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> validator.validateHearingRegistration(hearingRequest));
        assertEquals("Application Number: app1 does not exist ", exception.getMessage());
    }

    @Test
    void testValidateHearingExistence_Success() {
        // Arrange
        Hearing hearing = new Hearing();
        RequestInfo requestInfo = new RequestInfo();
        List<Hearing> existingHearings = Collections.singletonList(hearing);

        when(repository.checkHearingsExist(any())).thenReturn(existingHearings);

        // Act
        Hearing result = validator.validateHearingExistence(requestInfo,hearing);

        // Assert
        assertNotNull(result);
        assertEquals(hearing, result);
    }

    @Test
    void testValidateHearingExistence_NotFound() {
        // Arrange
        Hearing hearing = new Hearing();
        RequestInfo requestInfo = new RequestInfo();
        List<Hearing> existingHearings = Collections.emptyList();

        when(repository.getHearings(any())).thenReturn(existingHearings);

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> validator.validateHearingExistence(requestInfo,hearing));
        assertEquals("Hearing does not exist", exception.getMessage());
    }

    @Test
    void testCreateCaseExistsRequest_EmptyCriteria() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        Hearing hearing = new Hearing();

        // Act
        CaseExistsRequest request = validator.createCaseExistsRequest(requestInfo, hearing);

        // Assert
        assertNotNull(request);
        assertTrue(request.getCriteria().isEmpty());
    }

    @Test
    void testCreateApplicationExistsRequest_EmptyCriteria() {
        // Arrange
        RequestInfo requestInfo = new RequestInfo();
        Hearing hearing = new Hearing();

        // Act
        ApplicationExistsRequest request = validator.createApplicationExistRequest(requestInfo, hearing);

        // Assert
        assertNotNull(request);
        assertTrue(request.getApplicationExists().isEmpty());
    }

    @Test
    public void testValidateOrder_InvalidDocumentFileStore() {
        when(fileStoreUtil.doesFileExist(anyString(), anyString())).thenReturn(false);
        CustomException exception = assertThrows(CustomException.class, this::invokeValidator);
        assertEquals(INVALID_FILESTORE_ID, exception.getCode());
        assertEquals("Invalid document details", exception.getMessage());
    }

    private void invokeValidator() {
        Hearing hearing = new Hearing();
        hearing.setTenantId("pg");

        List<Hearing> existingHearings = Collections.singletonList(hearing);

        when(repository.checkHearingsExist(any())).thenReturn(existingHearings);

        Document document = new Document();
        document.setFileStore("invalidFileStore");
        hearing.setDocuments(Collections.singletonList(document));
        validator.validateHearingExistence(new RequestInfo(),hearing);
    }
}
