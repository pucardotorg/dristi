package digit.helper;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class QueryBuilderHelperTest {


    @InjectMocks
    private QueryBuilderHelper queryBuilderHelper;

    @Test
    void testAddClauseIfRequired_withEmptyPreparedStmtList() {
        // Arrange
        StringBuilder query = new StringBuilder("SELECT * FROM table");
        List<Object> preparedStmtList = new ArrayList<>();

        // Act
        queryBuilderHelper.addClauseIfRequired(query, preparedStmtList);

        // Assert
        assertEquals("SELECT * FROM table WHERE ", query.toString());
    }

    @Test
    void testAddClauseIfRequired_withNonEmptyPreparedStmtList() {
        // Arrange
        StringBuilder query = new StringBuilder("SELECT * FROM table");
        List<Object> preparedStmtList = new ArrayList<>(Collections.singletonList("value"));

        // Act
        queryBuilderHelper.addClauseIfRequired(query, preparedStmtList);

        // Assert
        assertEquals("SELECT * FROM table AND ", query.toString());
    }

    @Test
    void testCreateQuery_withMultipleIds() {
        // Arrange
        List<String> ids = Arrays.asList("id1", "id2", "id3");

        // Act
        String query = queryBuilderHelper.createQuery(ids);

        // Assert
        assertEquals(" ?, ?, ?", query);
    }

    @Test
    void testCreateQuery_withSingleId() {
        // Arrange
        List<String> ids = Collections.singletonList("id1");

        // Act
        String query = queryBuilderHelper.createQuery(ids);

        // Assert
        assertEquals(" ?", query);
    }

    @Test
    void testAddToPreparedStatement() {
        // Arrange
        List<Object> preparedStmtList = new ArrayList<>();
        List<String> ids = Arrays.asList("id1", "id2", "id3");

        // Act
        queryBuilderHelper.addToPreparedStatement(preparedStmtList, ids);

        // Assert
        assertEquals(Arrays.asList("id1", "id2", "id3"), preparedStmtList);
    }
}

