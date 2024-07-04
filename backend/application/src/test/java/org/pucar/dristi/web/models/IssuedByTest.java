package org.pucar.dristi.web.models;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class IssuedByTest {

    @Test
    public void testNoArgsConstructor() {
        IssuedBy issuedBy = new IssuedBy();
        assertThat(issuedBy.getBenchId()).isNull();
        assertThat(issuedBy.getJudgeId()).isNull();
        assertThat(issuedBy.getCourtId()).isNull();
    }

    @Test
    public void testAllArgsConstructor() {
        String benchId = "bench123";
        List<UUID> judgeId = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        String courtId = "court123";

        IssuedBy issuedBy = new IssuedBy(benchId, judgeId, courtId);

        assertEquals(issuedBy.getBenchId(), benchId);
        assertEquals(issuedBy.getJudgeId(), judgeId);
        assertEquals(issuedBy.getCourtId(), courtId);
    }

    @Test
    public void testBuilder() {
        String benchId = "bench123";
        List<UUID> judgeId = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        String courtId = "court123";

        IssuedBy issuedBy = IssuedBy.builder()
                .benchId(benchId)
                .judgeId(judgeId)
                .courtId(courtId)
                .build();

        assertEquals(issuedBy.getBenchId(), benchId);
        assertEquals(issuedBy.getJudgeId(), judgeId);
        assertEquals(issuedBy.getCourtId(), courtId);
    }

    @Test
    public void testSettersAndGetters() {
        String benchId = "bench123";
        List<UUID> judgeId = Arrays.asList(UUID.randomUUID(), UUID.randomUUID());
        String courtId = "court123";

        IssuedBy issuedBy = new IssuedBy();
        issuedBy.setBenchId(benchId);
        issuedBy.setJudgeId(judgeId);
        issuedBy.setCourtId(courtId);

        assertEquals(issuedBy.getBenchId(), benchId);
        assertEquals(issuedBy.getJudgeId(), judgeId);
        assertEquals(issuedBy.getCourtId(), courtId);
    }
}