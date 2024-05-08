package org.pucar.dristi.repository.querybuilder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.pucar.dristi.web.models.WitnessSearchCriteria;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class WitnessQueryBuilderTest {

    @InjectMocks
    private WitnessQueryBuilder witnessQueryBuilder;

    @Mock
    private List<Object> preparedStmtList;

    @BeforeEach
    public void setUp() {
        // Set up mock behavior or fields if needed
    }

    @Test
    public void testGetWitnessesSearchQuery_CaseIdCriteria() {
        // Arrange
        List<WitnessSearchCriteria> criteriaList = new ArrayList<>();
        criteriaList.add(new WitnessSearchCriteria("12345", null, false));

        // Act
        String query = witnessQueryBuilder.getWitnessesSearchQuery(criteriaList, preparedStmtList);

        // Assert
        assertEquals(" SELECT witness.id as id, witness.caseid as caseid, witness.filingnumber as filingnumber, witness.cnrnumber as cnrnumber, witness.witnessidentifier as witnessidentifier, witness.individualid as individualid,  witness.remarks as remarks, witness.isactive as isactive, witness.additionaldetails as additionaldetails, witness.createdby as createdby, witness.lastmodifiedby as lastmodifiedby, witness.createdtime as createdtime, witness.lastmodifiedtime as lastmodifiedtime  FROM dristi_witness witness WHERE witness.caseid IN (?) ORDER BY witness.createdtime DESC ", query);
    }

    // Other test methods remain the same
}
