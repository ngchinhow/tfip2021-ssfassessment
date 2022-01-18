package com.tfip2021.module2.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import static com.tfip2021.module2.model.Constants.REDIS_CACHED_TIME;

@Repository
public class BookRepository {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    public boolean hasCachedBook(String olid) {
        return redisTemplate.hasKey(olid);
    }

    public String getCachedBook(String olid) {
        return redisTemplate.opsForValue().get(olid);
    }

    public void setCachedBook(String olid, String value) {
        redisTemplate.opsForValue().set(olid, value);
        redisTemplate.expire(olid, REDIS_CACHED_TIME, TimeUnit.MINUTES);
    }
}
