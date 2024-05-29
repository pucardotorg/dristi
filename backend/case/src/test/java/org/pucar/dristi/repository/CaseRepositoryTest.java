package org.pucar.dristi.repository;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.repository.querybuilder.CaseQueryBuilder;
import org.pucar.dristi.repository.rowmapper.CaseRowMapper;
import org.pucar.dristi.web.models.CaseCriteria;
import org.pucar.dristi.web.models.CourtCase;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CaseRepositoryTest {

    @Mock
    private JdbcTemplate jdbcTemplate;
    @Mock
    private CaseQueryBuilder queryBuilder;
    @Mock
    private CaseRowMapper rowMapper;

    @InjectMocks
    private CaseRepository caseRepository;

    @BeforeEach
    void setUp() {
        // Setup necessary before each test
    }

    @Test
    void testGetCourtCases() {
        List<CaseCriteria> criteria = new ArrayList<>();
        String sql = "SELECT * FROM cases";
        CourtCase courtCase = new CourtCase();

        lenient().when(queryBuilder.getCasesSearchQuery(CaseCriteria.builder().filingNumber(courtCase.getFilingNumber()).caseId(String.valueOf(courtCase.getId()))
                .cnrNumber(courtCase.getCnrNumber()).courtCaseNumber(courtCase.getCourCaseNumber()).build(), new ArrayList<>())).thenReturn(sql);

        List<CaseCriteria> result = caseRepository.getApplications(criteria);

        assertNotNull(result);
    }
}

