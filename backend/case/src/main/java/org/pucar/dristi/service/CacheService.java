package org.pucar.dristi.service;

import org.pucar.dristi.config.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class CacheService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    private Configuration config;

    public void save(String id, Object value) {
        redisTemplate.opsForValue().set(id, value, config.getRedisTimeout(), TimeUnit.MINUTES);
    }

    public Object findById(String id) {
        return redisTemplate.opsForValue().get(id);
    }

    public void delete(String id) {
        redisTemplate.delete(id);
    }
}
