package org.pucar.repository.rowmapper;

import org.pucar.web.models.Advocate;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Component
public class AdvocateClerkRegistrationRowMapper implements ResultSetExtractor<List<Advocate>> {
    @Override
    public List<Advocate> extractData(ResultSet rs) throws SQLException, DataAccessException {
        return null;
    }

}