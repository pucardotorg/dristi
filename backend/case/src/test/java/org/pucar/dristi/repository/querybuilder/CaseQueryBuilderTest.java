package org.pucar.dristi.repository.querybuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.CaseCriteria;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CaseQueryBuilderTest {

    private CaseQueryBuilder queryBuilder;

    @BeforeEach
    void setUp() {
        queryBuilder = new CaseQueryBuilder();
    }

    @Test
    void testGetCasesSearchQuery_SingleCriteria() {
        CaseCriteria criteria = new CaseCriteria();
        criteria.setCaseId("12345");

        List<Object> preparedStmtList = new ArrayList<>();

        String expectedQuery = " SELECT cases.id as id, cases.tenantid as tenantid, cases.casenumber as casenumber, cases.resolutionmechanism as resolutionmechanism, cases.casetitle as casetitle, cases.casedescription as casedescription, cases.filingnumber as filingnumber, cases.casenumber as casenumber, cases.accesscode as accesscode, cases.courtcasenumber as courtcasenumber, cases.cnrNumber as cnrNumber,  cases.courtid as courtid, cases.benchid as benchid, cases.filingdate as filingdate, cases.registrationdate as registrationdate, cases.natureofpleading as natureofpleading, cases.status as status, cases.remarks as remarks, cases.isactive as isactive, cases.casedetails as casedetails, cases.additionaldetails as additionaldetails, cases.casecategory as casecategory, cases.createdby as createdby, cases.lastmodifiedby as lastmodifiedby, cases.createdtime as createdtime, cases.lastmodifiedtime as lastmodifiedtime  FROM dristi_cases cases WHERE cases.id IN (?) ORDER BY cases.createdtime DESC ";
        String actualQuery = queryBuilder.getCasesSearchQuery(criteria, preparedStmtList);

        assertEquals(expectedQuery, actualQuery);
        assertEquals(1, preparedStmtList.size());
        assertEquals("12345", preparedStmtList.get(0));
    }

}

