package org.pucar.dristi.repository;

import org.egov.common.contract.models.Document;
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
import org.pucar.dristi.web.models.Application;
import org.pucar.dristi.web.models.ApplicationExists;
import org.pucar.dristi.web.models.StatuteSection;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
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
        when(queryBuilder.getApplicationSearchQuery(anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn("some SQL query");
        when(jdbcTemplate.query(anyString(), any(ApplicationRowMapper.class))).thenReturn(applicationList);

        when(queryBuilder.getStatuteSectionSearchQuery(anyList(), anyList()))
                .thenReturn("statute section SQL query");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(StatuteSectionRowMapper.class)))
                .thenReturn(statuteSectionsMap);

        when(queryBuilder.getDocumentSearchQuery(anyList(), anyList()))
                .thenReturn("document SQL query");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(DocumentRowMapper.class)))
                .thenReturn(documentMap);

        List<Application> result = applicationRepository.getApplications("1", "123", "CNR123", "tenant1", "status1", 10, 0);

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(applicationList.size(), result.size());
        assertEquals(documentList, result.get(0).getDocuments());
        assertEquals(statuteSectionsMap.get(applicationList.get(0).getId()), result.get(0).getStatuteSection());
    }

    @Test
    void testGetApplications_EmptyResult() {
        when(queryBuilder.getApplicationSearchQuery(anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenReturn("some SQL query");
        when(jdbcTemplate.query(anyString(), any(ApplicationRowMapper.class))).thenReturn(Collections.emptyList());

        List<Application> result = applicationRepository.getApplications("1", "123", "CNR123", "tenant1", "status1", 10, 0);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetApplications_Exception() {
        when(queryBuilder.getApplicationSearchQuery(anyString(), anyString(), anyString(), anyString(), anyString(), anyInt(), anyInt()))
                .thenThrow(new RuntimeException("Database error"));

        CustomException exception = assertThrows(CustomException.class, () ->
                applicationRepository.getApplications("1", "123", "CNR123", "tenant1",null,  10, 0)
        );

        assertEquals(APPLICATION_SEARCH_ERR, exception.getCode());
        String expectedMessage = "Error while fetching application list:";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
    @Test
    public void testGetApplications_ThrowsCustomException() {
        // Arrange
        String id = "testId";
        String filingNumber = "testFilingNumber";
        String cnrNumber = "testCnrNumber";
        String tenantId = "testTenantId";
        String status = "status";
        Integer limit = 10;
        Integer offset = 0;

        doThrow(new RuntimeException("Database error")).when(jdbcTemplate).query(anyString(), any(ApplicationRowMapper.class));

        // Act & Assert
        CustomException exception = assertThrows(CustomException.class, () -> {
            applicationRepository.getApplications(id, filingNumber, cnrNumber, tenantId, status, limit, offset);
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
}
