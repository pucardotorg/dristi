package drishti.payment.calculator.repository.querybuilder;


import drishti.payment.calculator.web.models.PostalServiceSearchCriteria;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class PostalServiceQueryBuilderTest {

    @InjectMocks
    private PostalServiceQueryBuilder postalServiceQueryBuilder;

    @Test
    public void testGetPostalServiceQuery() {
        PostalServiceSearchCriteria criteria = new PostalServiceSearchCriteria();
        Integer limit = 10;
        Integer offset = 0;
        List<Object> preparedStmtList = new ArrayList<>();

        String query = postalServiceQueryBuilder.getPostalServiceQuery(criteria, preparedStmtList, new ArrayList<>());

        assertNotNull(query);
        assertTrue(query.contains("SELECT *  FROM postal_service ps"));
    }
}
