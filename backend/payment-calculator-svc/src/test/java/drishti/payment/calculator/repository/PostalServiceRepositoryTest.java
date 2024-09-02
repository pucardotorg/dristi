package drishti.payment.calculator.repository;

import drishti.payment.calculator.repository.querybuilder.PostalServiceQueryBuilder;
import drishti.payment.calculator.repository.rowmapper.PostalServiceRowMapper;
import drishti.payment.calculator.web.models.PostalService;
import drishti.payment.calculator.web.models.PostalServiceSearchCriteria;
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
public class PostalServiceRepositoryTest {

    @InjectMocks
    private PostalServiceRepository postalServiceRepository;

    @Mock
    private PostalServiceRowMapper rowMapper;

    @Mock
    private PostalServiceQueryBuilder queryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Test
    public void testGetPostalService() {
        PostalServiceSearchCriteria criteria = new PostalServiceSearchCriteria();
        Integer limit = 10;
        Integer offset = 0;

        PostalService postalService = new PostalService();

        when(queryBuilder.getPostalServiceQuery(criteria, new ArrayList<>(), new ArrayList<>())).thenReturn("query");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(), any(PostalServiceRowMapper.class))).thenReturn(Collections.singletonList(postalService));

        List<PostalService> postalServices = postalServiceRepository.getPostalService(criteria);

        assertEquals(Collections.singletonList(postalService), postalServices);
    }
}
