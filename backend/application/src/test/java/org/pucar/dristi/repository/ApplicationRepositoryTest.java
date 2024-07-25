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

    @InjectMocks
    private ApplicationRepository applicationRepository;

    private List<Document> documentList;
    private Map<UUID, StatuteSection> statuteSectionsMap;
    private Map<UUID, List<Document>> documentMap;

    @BeforeEach
    void setUp() {
        // Setting up sample data
        Application application = new Application();
        application.setId(UUID.randomUUID());

        Document document = new Document();
        documentList = Collections.singletonList(document);

        StatuteSection statuteSection = new StatuteSection();
        statuteSectionsMap = new HashMap<>();
        statuteSectionsMap.put(application.getId(), statuteSection);

        documentMap = new HashMap<>();
        documentMap.put(application.getId(), documentList);
    }

    @Test
    void testGetApplicationsSuccess() {
        ApplicationSearchRequest searchRequest = new ApplicationSearchRequest();
        ApplicationCriteria criteria = new ApplicationCriteria();
        String applicationId = UUID.randomUUID().toString();
        criteria.setId(applicationId);

        String applicationType = "type1"; // Set an example application type
        criteria.setApplicationType(applicationType); // Ensure the application type is set in the criteria

        searchRequest.setCriteria(criteria);
        Pagination pagination = new Pagination();
        searchRequest.setPagination(pagination);

        String applicationQuery = "SELECT * FROM applications";
        String documentQuery = "SELECT * FROM documents WHERE applicationId IN (?)";
        String countQuery = "SELECT COUNT(*) FROM applications";

        List<Application> applications = Arrays.asList(new Application());
        applications.get(0).setId(UUID.randomUUID());

        // Use eq and any to match arguments
        when(queryBuilder.getApplicationSearchQuery(
                any(), anyList()))
                .thenReturn(applicationQuery);
        when(queryBuilder.addOrderByQuery(anyString(), any(Pagination.class)))
                .thenReturn(applicationQuery + " ORDER BY createdTime");
        when(queryBuilder.addPaginationQuery(anyString(), any(Pagination.class), anyList()))
                .thenReturn(applicationQuery + " ORDER BY createdTime LIMIT 10 OFFSET 0");
        when(queryBuilder.getTotalCountQuery(anyString())).thenReturn(countQuery);

        when(jdbcTemplate.query(eq(applicationQuery + " ORDER BY createdTime LIMIT 10 OFFSET 0"), any(Object[].class), eq(rowMapper)))
                .thenReturn(applications);
        when(jdbcTemplate.queryForObject(eq(countQuery), any(Object[].class), eq(Integer.class)))
                .thenReturn(1);

        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList())).thenReturn(documentQuery);
        when(jdbcTemplate.query(eq(documentQuery), any(Object[].class), eq(documentRowMapper)))
                .thenReturn(new HashMap<>());
        List<Application> result = applicationRepository.getApplications(searchRequest);

        assertNotNull(result);
        assertEquals(applications, result);
        assertEquals(1, pagination.getTotalCount());

        verify(queryBuilder, times(1)).getApplicationSearchQuery(any(), anyList());
        verify(queryBuilder, times(1)).addOrderByQuery(anyString(), any(Pagination.class));
        verify(queryBuilder, times(1)).addPaginationQuery(anyString(), any(Pagination.class), anyList());
        verify(queryBuilder, times(1)).getTotalCountQuery(anyString());

        verify(jdbcTemplate, times(1)).query(eq(applicationQuery + " ORDER BY createdTime LIMIT 10 OFFSET 0"), any(Object[].class), eq(rowMapper));
        verify(jdbcTemplate, times(1)).queryForObject(eq(countQuery), any(Object[].class), eq(Integer.class));
        verify(jdbcTemplate, times(1)).query(eq(documentQuery), any(Object[].class), eq(documentRowMapper));
    }




    @Test
    public void testGetApplicationsThrowsCustomException() {
        ApplicationSearchRequest searchRequest = new ApplicationSearchRequest();
        ApplicationCriteria criteria = new ApplicationCriteria();
        searchRequest.setCriteria(criteria);
        Pagination pagination = new Pagination();
        searchRequest.setPagination(pagination);

        when(queryBuilder.getApplicationSearchQuery(any(), any()))
                .thenThrow(new CustomException("TEST_ERROR", "Test error"));

        CustomException exception = assertThrows(CustomException.class, () ->
                applicationRepository.getApplications(searchRequest));

        assertEquals("TEST_ERROR", exception.getCode());
        assertEquals("Test error", exception.getMessage());
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
        lenient().when(queryBuilder.getApplicationSearchQuery(any(), any()))
                .thenReturn("some SQL query");
        lenient().when(jdbcTemplate.query(anyString(),any(Objects[].class), any(ApplicationRowMapper.class))).thenReturn(Collections.emptyList());

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

        when(queryBuilder.getApplicationSearchQuery(any(), any()))
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

        when(queryBuilder.checkApplicationExistQuery(any(), any(), any(), any())).thenReturn("SELECT COUNT(*) FROM applications WHERE filing_number = '123'");
        when(jdbcTemplate.queryForObject(any(), any(Object[].class), eq(Integer.class))).thenReturn(1);

        List<ApplicationExists> result = applicationRepository.checkApplicationExists(applicationExistsList);

        assertTrue(result.get(0).getExists());
    }

    @Test
    void testCheckApplicationExists_CnrNumber() {
        List<ApplicationExists> applicationExistsList = new ArrayList<>();
        applicationExistsList.add(new ApplicationExists(null, "456", null, null));

        when(queryBuilder.checkApplicationExistQuery(any(), any(), any(), any())).thenReturn("SELECT COUNT(*) FROM applications WHERE cnr_number = '456'");
        when(jdbcTemplate.queryForObject(any(), any(Object[].class), eq(Integer.class))).thenReturn(1);

        List<ApplicationExists> result = applicationRepository.checkApplicationExists(applicationExistsList);

        assertTrue(result.get(0).getExists());
    }

    @Test
    void testCheckApplicationExists_ApplicationNumber() {
        List<ApplicationExists> applicationExistsList = new ArrayList<>();
        applicationExistsList.add(new ApplicationExists(null, null, "789", null));

        when(queryBuilder.checkApplicationExistQuery(any(), any(), any(), any())).thenReturn("SELECT COUNT(*) FROM applications WHERE application_number = '789'");
        when(jdbcTemplate.queryForObject(any(), any(Object[].class), eq(Integer.class))).thenReturn(1);

        List<ApplicationExists> result = applicationRepository.checkApplicationExists(applicationExistsList);

        assertTrue(result.get(0).getExists());
    }

    @Test
    void testCheckApplicationExists_Exception() {
        List<ApplicationExists> applicationExistsList = new ArrayList<>();
        applicationExistsList.add(new ApplicationExists("123", "456", "789", null));

        when(queryBuilder.checkApplicationExistQuery(any(), any(), any(), any())).thenReturn("SELECT COUNT(*) FROM applications WHERE filing_number = '123' AND cnr_number = '456' AND application_number = '789'");
        when(jdbcTemplate.queryForObject(any(), any(Object[].class), eq(Integer.class))).thenThrow(new RuntimeException("Database connection failed"));

        assertThrows(CustomException.class, () -> applicationRepository.checkApplicationExists(applicationExistsList));
    }

    @Test
    void testCheckApplicationExists_Throws_CustomException() {
        List<ApplicationExists> applicationExistsList = new ArrayList<>();
        applicationExistsList.add(new ApplicationExists("123", "456", "789", null));

        when(queryBuilder.checkApplicationExistQuery(any(), any(), any(), any())).thenReturn("SELECT COUNT(*) FROM applications WHERE filing_number = '123' AND cnr_number = '456' AND application_number = '789'");
        when(jdbcTemplate.queryForObject(any(), any(Object[].class), eq(Integer.class))).thenThrow(new CustomException(APPLICATION_EXIST_EXCEPTION, "Error occurred while building the application exist query : " ));

        assertThrows(CustomException.class, () -> applicationRepository.checkApplicationExists(applicationExistsList));
    }
    @Test
    void testGetTotalCountApplication() {
        String baseQuery = "SELECT * FROM applications";
        String countQuery = "SELECT COUNT(*) FROM applications";
        List<Object> preparedStmtList = new ArrayList<>();

        lenient().when(queryBuilder.getTotalCountQuery(baseQuery)).thenReturn(countQuery);
        lenient().when(jdbcTemplate.queryForObject(eq(countQuery), any(Object[].class), eq(Integer.class))).thenReturn(42);
        Integer result = applicationRepository.getTotalCountApplication(baseQuery, preparedStmtList);

        assertNotNull(result);
        assertEquals(42, result);

        verify(queryBuilder, times(1)).getTotalCountQuery(baseQuery);
    }
}
