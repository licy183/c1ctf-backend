package club.c1sec.c1ctfplatform.limiter;

import club.c1sec.c1ctfplatform.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailRateLimiter extends BasicRateLimiter {
    Callback callback = null;

    @Override
    public String getPrefix() {
        return "MAIL_LIMITER_";
    }

    @Override
    public Integer getRatePerMin() {
        return null;
    }

    @Override
    public Integer getInterval() {
        return 30;
    }

    @Override
    public Callback getCallback() {
        return this.callback;
    }

    @Autowired
    public void setRedisService(RedisService redisService) {
        this.redisService = redisService;
    }
}
