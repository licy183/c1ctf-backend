package club.c1sec.c1ctfplatform.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    @Autowired
    private StringRedisTemplate redis;

    public void setKeyValueWithExpire(String key, String value, Integer time) {
        redis.opsForValue().set(key, value, time, TimeUnit.SECONDS);
    }

    public void setKeyValue(String key, String value) {
        redis.opsForValue().set(key, value);
    }

    public Boolean isKeyExist(String key) {
        return redis.opsForValue().get(key) != null;
    }

    public Long getRemainExpire(String key) {
        return redis.getExpire(key, TimeUnit.SECONDS);
    }

    public String getValue(String key) {
        return redis.opsForValue().get(key);
    }

    public void deleteKey(String key) {
        redis.delete(key);
    }
}
