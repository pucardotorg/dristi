package org.pucar.dristi.repository;

import org.egov.common.contract.models.Document;
import org.egov.common.contract.request.RequestInfo;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.repository.queryBuilder.ApplicationQueryBuilder;
import org.pucar.dristi.repository.rowMapper.ApplicationRowMapper;
import org.pucar.dristi.repository.rowMapper.DocumentRowMapper;
import org.pucar.dristi.repository.rowMapper.StatuteSectionRowMapper;
import org.pucar.dristi.web.models.*;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.pucar.dristi.config.ServiceConstants.APPLICATION_EXIST_EXCEPTION;
import static org.pucar.dristi.config.ServiceConstants.APPLICATION_SEARCH_ERR;

@ExtendWith(MockitoExtension.class)
class ApplicationRepositoryTest {

    @Mock
    private ApplicationQueryBuilder queryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private ApplicationRowMapper rowMapper;

    @Mock
    private DocumentRowMapper documentRowMapper;

    @Mock
    private StatuteSectionRowMapper statuteSectionRowMapper;

    @InjectMocks
    private ApplicationRepository applicationRepository;

    private List<Application> applicationList;
    private List<Document> documentList;
    private Map<UUID, StatuteSection> statuteSectionsMap;
    private Map<UUID, List<Document>> documentMap;

    @BeforeEach
    void setUp() {
        // Setting up sample data
        Application application = new Application();
        application.setId(UUID.randomUUID());
        applicationList = Collections.singletonList(application);

        Document document = new Document();
        documentList = Collections.singletonList(document);

        StatuteSection statuteSection = new StatuteSection();
        statuteSectionsMap = new HashMap<>();
        statuteSectionsMap.put(application.getId(), statuteSection);

        documentMap = new HashMap<>();
        documentMap.put(application.getId(), documentList);
    }

    @Test
    void testGetApplications_Success() {

        ApplicationSearchRequest applicationSearchRequest = new ApplicationSearchRequest();
        applicationSearchRequest.setRequestInfo(new RequestInfo());
        ApplicationCriteria applicationCriteria = new ApplicationCriteria();
        applicationCriteria.setApplicationNumber("");
        applicationCriteria.setId("");
        applicationCriteria.setStatus("");
        applicationCriteria.setCnrNumber("");
        applicationCriteria.setFilingNumber("");
        applicationCriteria.setTenantId("");
        applicationSearchRequest.setCriteria(applicationCriteria);
        Pagination pagination = new Pagination();
        pagination.setOffSet(0d);
        pagination.setLimit(10d);
        applicationSearchRequest.setPagination(pagination);

        when(queryBuilder.getApplicationSearchQuery(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("some SQL query");
        when(queryBuilder.addPaginationQuery(anyString(), any()))
                .thenReturn("some SQL query");
        when(jdbcTemplate.query(anyString(), any(ApplicationRowMapper.class))).thenReturn(applicationList);
        when(applicationRepository.getTotalCountApplication(anyString())).thenReturn(1);
        when(queryBuilder.getStatuteSectionSearchQuery(anyList(), anyList()))
                .thenReturn("statute section SQL query");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(StatuteSectionRowMapper.class)))
                .thenReturn(statuteSectionsMap);

        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList()))
                .thenReturn("document SQL query");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(DocumentRowMapper.class)))
                .thenReturn(documentMap);
        List<Application> result = applicationRepository.getApplications(applicationSearchRequest );

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertTrue(applicationList.size() <= pagination.getLimit());
        assertEquals(applicationList.size(), result.size());
        assertEquals(documentList, result.get(0).getDocuments());
        assertEquals(statuteSectionsMap.get(applicationList.get(0).getId()), result.get(0).getStatuteSection());
    }

    @Test
    void testGetApplications_EmptyResult() {
        ApplicationSearchRequest applicationSearchRequest = new ApplicationSearchRequest();
        applicationSearchRequest.setRequestInfo(new RequestInfo());
        ApplicationCriteria applicationCriteria = new ApplicationCriteria();
        applicationCriteria.setApplicationNumber("");
        applicationCriteria.setId("");
        applicationCriteria.setStatus("");
        applicationCriteria.setCnrNumber("");
        applicationCriteria.setFilingNumber("");
        applicationCriteria.setTenantId("");
        applicationSearchRequest.setCriteria(applicationCriteria);
        when(queryBuilder.getApplicationSearchQuery(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn("some SQL query");
        when(jdbcTemplate.query(anyString(), any(ApplicationRowMapper.class))).thenReturn(Collections.emptyList());

        List<Application> result = applicationRepository.getApplications(applicationSearchRequest);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetApplications_Exception() {
        ApplicationSearchRequest applicationSearchRequest = new ApplicationSearchRequest();
        applicationSearchRequest.setRequestInfo(new RequestInfo());
        ApplicationCriteria applicationCriteria = new ApplicationCriteria();
        applicationCriteria.setApplicationNumber("");
        applicationCriteria.setId("");
        applicationCriteria.setStatus("");
        applicationCriteria.setCnrNumber("");
        applicationCriteria.setFilingNumber("");
        applicationCriteria.setTenantId("");
        applicationSearchRequest.setCriteria(applicationCriteria);

        when(queryBuilder.getApplicationSearchQuery(anyString(), anyString(), anyString(), anyString(), anyString(),anyString()))
                .thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () ->
                applicationRepository.getApplications(applicationSearchRequest)
        );

        assertEquals(APPLICATION_SEARCH_ERR, exception.getCode());
        String expectedMessage = "Error while fetching application list:";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    void testGetApplications_ThrowsCustomException() {
        // Arrange
        ApplicationSearchRequest applicationSearchRequest = new ApplicationSearchRequest();
        applicationSearchRequest.setRequestInfo(new RequestInfo());
        applicationSearchRequest.setCriteria(new ApplicationCriteria());

        doThrow(new RuntimeException("Database error")).when(jdbcTemplate).query(anyString(), any(ApplicationRowMapper.class));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            applicationRepository.getApplications(applicationSearchRequest);
        });

        // Assert
        String expectedMessage = "Error while fetching application list:";
        String actualMessage = exception.getMessage();
        assert actualMessage.contains(expectedMessage);
    }
    @Test
    void testCheckApplicationExists_AllEmpty() {
        List<ApplicationExists> applicationExistsList = new ArrayList<>();
        applicationExistsList.add(new ApplicationExists(null, null, null, null));

        List<ApplicationExists> result = applicationRepository.checkApplicationExists(applicationExistsList);

        assertFalse(result.get(0).getExists());
    }
    @Test
    void testCheckApplicationExists_FilingNumber() {
        List<ApplicationExists> applicationExistsList = new ArrayList<>();
        applicationExistsList.add(new ApplicationExists("123", null, null, null));

        when(queryBuilder.checkApplicationExistQuery(any(), any(), any())).thenReturn("SELECT COUNT(*) FROM applications WHERE filing_number = '123'");
        when(jdbcTemplate.queryForObject(any(), eq(Integer.class))).thenReturn(1);

        List<ApplicationExists> result = applicationRepository.checkApplicationExists(applicationExistsList);

        assertTrue(result.get(0).getExists());
    }

    @Test
    void testCheckApplicationExists_CnrNumber() {
        List<ApplicationExists> applicationExistsList = new ArrayList<>();
        applicationExistsList.add(new ApplicationExists(null, "456", null, null));

        when(queryBuilder.checkApplicationExistQuery(any(), any(), any())).thenReturn("SELECT COUNT(*) FROM applications WHERE cnr_number = '456'");
        when(jdbcTemplate.queryForObject(any(), eq(Integer.class))).thenReturn(1);

        List<ApplicationExists> result = applicationRepository.checkApplicationExists(applicationExistsList);

        assertTrue(result.get(0).getExists());
    }

    @Test
    void testCheckApplicationExists_ApplicationNumber() {
        List<ApplicationExists> applicationExistsList = new ArrayList<>();
        applicationExistsList.add(new ApplicationExists(null, null, "789", null));

        when(queryBuilder.checkApplicationExistQuery(any(), any(), any())).thenReturn("SELECT COUNT(*) FROM applications WHERE application_number = '789'");
        when(jdbcTemplate.queryForObject(any(), eq(Integer.class))).thenReturn(1);

        List<ApplicationExists> result = applicationRepository.checkApplicationExists(applicationExistsList);

        assertTrue(result.get(0).getExists());
    }

    @Test
    void testCheckApplicationExists_Exception() {
        List<ApplicationExists> applicationExistsList = new ArrayList<>();
        applicationExistsList.add(new ApplicationExists("123", "456", "789", null));

        when(queryBuilder.checkApplicationExistQuery(any(), any(), any())).thenReturn("SELECT COUNT(*) FROM applications WHERE filing_number = '123' AND cnr_number = '456' AND application_number = '789'");
        when(jdbcTemplate.queryForObject(any(), eq(Integer.class))).thenThrow(new RuntimeException("Database connection failed"));

        assertThrows(CustomException.class, () -> applicationRepository.checkApplicationExists(applicationExistsList));
    }

    @Test
    void testCheckApplicationExists_Throws_CustomException() {
        List<ApplicationExists> applicationExistsList = new ArrayList<>();
        applicationExistsList.add(new ApplicationExists("123", "456", "789", null));

        when(queryBuilder.checkApplicationExistQuery(any(), any(), any())).thenReturn("SELECT COUNT(*) FROM applications WHERE filing_number = '123' AND cnr_number = '456' AND application_number = '789'");
        when(jdbcTemplate.queryForObject(any(), eq(Integer.class))).thenThrow(new CustomException(APPLICATION_EXIST_EXCEPTION, "Error occurred while building the application exist query : " ));

        assertThrows(CustomException.class, () -> applicationRepository.checkApplicationExists(applicationExistsList));
    }
    @Test
    void testGetTotalCountApplication() {
        String baseQuery = "SELECT * FROM applications";
        String countQuery = "SELECT COUNT(*) FROM applications";

        when(queryBuilder.getTotalCountQuery(baseQuery)).thenReturn(countQuery);
        when(jdbcTemplate.queryForObject(countQuery, Integer.class)).thenReturn(42);

        Integer result = applicationRepository.getTotalCountApplication(baseQuery);

        assertNotNull(result);
        assertEquals(42, result);

        verify(queryBuilder, times(1)).getTotalCountQuery(baseQuery);
        verify(jdbcTemplate, times(1)).queryForObject(countQuery, Integer.class);
    }
}
