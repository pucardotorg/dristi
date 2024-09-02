package digit.repository;

import digit.repository.querybuilder.SummonsDeliveryQueryBuilder;
import digit.repository.rowmapper.SummonsDeliveryRowMapper;
import digit.web.models.SummonsDelivery;
import digit.web.models.SummonsDeliverySearchCriteria;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SummonsReposiotoryTest {

    @InjectMocks
    private SummonsRepository summonsRepository;

    @Mock
    private SummonsDeliverySearchCriteria searchCriteria;

    @Mock
    private SummonsDeliveryQueryBuilder queryBuilder;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @Mock
    private SummonsDeliveryRowMapper rowMapper;

    @Test
    public void testGetSummons() {
        // Arrange
        when(queryBuilder.getSummonsQuery(searchCriteria, new ArrayList<>())).thenReturn("SELECT * FROM summons");
        when(jdbcTemplate.query("SELECT * FROM summons", new ArrayList<>().toArray(), rowMapper)).thenReturn(new ArrayList<>());
        // Act
        List<SummonsDelivery> result = summonsRepository.getSummons(searchCriteria);

        // Assert

        Assertions.assertNotNull(result);
        }
}
