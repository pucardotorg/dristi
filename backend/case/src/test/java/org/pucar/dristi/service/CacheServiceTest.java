package org.pucar.dristi.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.pucar.dristi.config.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CacheServiceTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private Configuration config;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private CacheService cacheService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    public void testSave() {
        String id = "testId";
        Object value = "testValue";
        long timeout = 10L;

        when(config.getRedisTimeout()).thenReturn(timeout);

        cacheService.save(id, value);

        verify(valueOperations, times(1)).set(id, value, timeout, TimeUnit.MINUTES);
        verify(config, times(1)).getRedisTimeout();
    }

    @Test
    public void testFindById() {
        String id = "testId";
        Object expectedValue = "testValue";

        when(valueOperations.get(id)).thenReturn(expectedValue);

        Object result = cacheService.findById(id);

        assertEquals(expectedValue, result);
        verify(valueOperations, times(1)).get(id);
    }

    @Test
    public void testDelete() {
        String id = "testId";

        cacheService.delete(id);

        verify(redisTemplate, times(1)).delete(id);
    }
}
