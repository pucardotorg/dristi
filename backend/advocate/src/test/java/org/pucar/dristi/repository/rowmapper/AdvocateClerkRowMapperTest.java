package org.pucar.dristi.repository.rowmapper;

import org.egov.common.contract.models.Document;
import org.egov.tracer.model.CustomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.AdvocateClerk;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
 class AdvocateClerkRowMapperTest {

    @Mock
    private ResultSet rs;

    private AdvocateClerkRowMapper rowMapper;

    @BeforeEach
    void setUp() {
        rowMapper = new AdvocateClerkRowMapper();
    }

    @Test
    void shouldExtractDataSuccessfully() throws SQLException {
        // Arrange
        lenient().when(rs.next()).thenReturn(true).thenReturn(false);
        lenient().when(rs.getString("applicationnumber")).thenReturn("1234");
        lenient().when(rs.getString("tenantid")).thenReturn("tenant1");
        lenient().when(rs.getString("id")).thenReturn(UUID.randomUUID().toString());
        lenient().when(rs.getString("stateregnnumber")).thenReturn("bar123");
        lenient().when(rs.getString("organisationid")).thenReturn(UUID.randomUUID().toString());
        lenient().when(rs.getString("individualid")).thenReturn("ind123");
        lenient().when(rs.getString("isactive")).thenReturn("true");
        lenient().when(rs.getString("advocatetype")).thenReturn("type1");
        lenient().when(rs.wasNull()).thenReturn(false);

        // Act
        List<AdvocateClerk> results = rowMapper.extractData(rs);

        // Assert
        assertNotNull(results);
        assertEquals(1, results.size());
        AdvocateClerk result = results.get(0);
        assertEquals("1234", result.getApplicationNumber());
        assertEquals("tenant1", result.getTenantId());
        assertEquals("bar123", result.getStateRegnNumber());
    }

    @Test
    void addDocumentToApplication_Success() throws SQLException{
        AdvocateClerk advocateClerk = new AdvocateClerk();
        Document document = new Document();
        document.setId("1");
        document.setDocumentType("Type");
        document.setFileStore("FileStore");
        document.setDocumentUid("UID");
        document.setAdditionalDetails("Details");
        List<Document> list = new ArrayList<>();
        list.add(document);
        advocateClerk.setDocuments(list);
        // Arrange
        when(rs.getString("aid")).thenReturn("1");
        when(rs.getString("document_type")).thenReturn("Type");
        when(rs.getString("filestore")).thenReturn("FileStore");
        when(rs.getString("document_uid")).thenReturn("UID");
        when(rs.getObject("additional_details")).thenReturn("Details");

        // Act
        rowMapper.addDocumentToApplication(rs, advocateClerk);

        // Assert
        List<Document> documents = advocateClerk.getDocuments();
        assertNotNull(documents);
        assertEquals(1, documents.size());
        Document documentBis = documents.get(0);
        assertEquals("1", documentBis.getId());
        assertEquals("Type", documentBis.getDocumentType());
        assertEquals("FileStore", documentBis.getFileStore());
        assertEquals("UID", documentBis.getDocumentUid());
        assertEquals("Details", documentBis.getAdditionalDetails());
    }

    @Test
    void addDocumentToApplication_Exception() throws SQLException{
        // Arrange
        AdvocateClerk advocateClerk = new AdvocateClerk();
        Document document = new Document();
        document.setId("1");
        document.setDocumentType("Type");
        document.setFileStore("FileStore");
        document.setDocumentUid("UID");
        document.setAdditionalDetails("Details");
        List<Document> list = new ArrayList<>();
        list.add(document);
        advocateClerk.setDocuments(list);

        when(rs.getString(anyString())).thenThrow(new RuntimeException("Error"));

        // Act and Assert
        assertThrows(CustomException.class, () -> {
            rowMapper.addDocumentToApplication(rs, advocateClerk);
        });
    }

    @Test
    void addDocumentToApplication_CustomException() throws SQLException{
        // Arrange
        AdvocateClerk advocateClerk = new AdvocateClerk();
        Document document = new Document();
        document.setId("1");
        document.setDocumentType("Type");
        document.setFileStore("FileStore");
        document.setDocumentUid("UID");
        document.setAdditionalDetails("Details");
        List<Document> list = new ArrayList<>();
        list.add(document);
        advocateClerk.setDocuments(list);

        when(rs.getString(anyString())).thenThrow(new CustomException());

        // Act and Assert
        assertThrows(CustomException.class, () -> rowMapper.addDocumentToApplication(rs, advocateClerk));
    }

    @Test
    void testExtractData_Exception() throws Exception {
        when(rs.next()).thenThrow(new SQLException());

        assertThrows(Exception.class, () -> rowMapper.extractData(rs));
    }

    @Test
    void testExtractData_CustomException() throws Exception {
        when(rs.next()).thenThrow(new CustomException());

        assertThrows(CustomException.class, () -> rowMapper.extractData(rs));
    }
}
