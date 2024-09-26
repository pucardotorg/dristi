package drishti.payment.calculator.repository;


import drishti.payment.calculator.repository.querybuilder.PostalHubQueryBuilder;
import drishti.payment.calculator.repository.rowmapper.PostalHubRowMapper;
import drishti.payment.calculator.web.models.HubSearchCriteria;
import drishti.payment.calculator.web.models.PostalHub;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostalHubRepositoryTest {

    @InjectMocks
    private PostalHubRepository postalHubRepository;

    @Mock
    private PostalHubRowMapper rowMapper;

    @Mock
    private PostalHubQueryBuilder queryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;


    @Test
    public void getPostalHubReturnsExpectedResult() {
        HubSearchCriteria criteria = new HubSearchCriteria();
        PostalHub postalHub = new PostalHub();
        when(queryBuilder.getPostalHubQuery(criteria, Collections.emptyList(), new ArrayList<>())).thenReturn("query");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(), any(PostalHubRowMapper.class))).thenReturn(Collections.singletonList(postalHub));

        List<PostalHub> result = postalHubRepository.getPostalHub(criteria);

        assertEquals(Collections.singletonList(postalHub), result);
    }
}
