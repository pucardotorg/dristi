package com.egov.icops_integrationkerala.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;


@ExtendWith(MockitoExtension.class)
class IcopsQueryBuilderTest {

    @InjectMocks
    private IcopsQueryBuilder icopsQueryBuilder;

    @Test
    void testGetIcopsTracker() {
        String processUniqueId = "processUniqueId";
        List<Object> preparedStmtList = new ArrayList<>();
        String result = icopsQueryBuilder.getIcopsTracker(processUniqueId, preparedStmtList);
        assertNotNull(result);
    }

    @Test
    void testGetIcopsTracker_1() {
        List<Object> preparedStmtList = new ArrayList<>();
        String result = icopsQueryBuilder.getIcopsTracker(null, preparedStmtList);
        assertNotNull(result);
    }

    @Test
    void testGetIcopsTracker_2() {
        String processUniqueId = "processUniqueId";
        List<Object> preparedStmtList = new ArrayList<>();
        preparedStmtList.add("processUniqueId");
        String result = icopsQueryBuilder.getIcopsTracker(processUniqueId, preparedStmtList);
        assertNotNull(result);
    }
}