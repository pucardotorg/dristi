package digit.repository;

import digit.repository.querybuilder.CauseListQueryBuilder;
import digit.repository.rowmapper.CauseListRowMapper;
import digit.web.models.CauseList;
import digit.web.models.CauseListSearchCriteria;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CauseListRepositoryTest {

    @Mock
    private CauseListQueryBuilder queryBuilder;

    @Mock
    private CauseListRowMapper rowMapper;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private CauseListRepository causeListRepository;

    private CauseListSearchCriteria searchCriteria;
    private List<CauseList> causeLists;

    @BeforeEach
    void setUp(){
        searchCriteria = new CauseListSearchCriteria();
        causeLists = List.of(new CauseList());
    }

    @Test
    public void getCauseList_Success(){
        searchCriteria.setSearchDate(LocalDate.now());
        searchCriteria.setCourtId("court");

        when(queryBuilder.getCauseListQuery(searchCriteria, List.of())).thenReturn("SELECT * FROM cause_list");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(CauseListRowMapper.class))).thenReturn(causeLists);

        causeLists = causeListRepository.getCauseLists(searchCriteria);

        assertEquals(1, causeLists.size());
        verify(queryBuilder).getCauseListQuery(searchCriteria, List.of());
        verify(jdbcTemplate).query(anyString(), any(Object[].class), any(CauseListRowMapper.class));

    }

    @Test
    public void getCauseList_Empty(){
        when(queryBuilder.getCauseListQuery(searchCriteria, List.of())).thenReturn("SELECT * FROM cause_list");
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(CauseListRowMapper.class))).thenReturn(List.of());

        causeLists = causeListRepository.getCauseLists(searchCriteria);

        assertEquals(0, causeLists.size());
        verify(queryBuilder).getCauseListQuery(searchCriteria, List.of());
        verify(jdbcTemplate).query(anyString(), any(Object[].class), any(CauseListRowMapper.class));
    }

}
