package club.c1sec.c1ctfplatform.limiter;

import club.c1sec.c1ctfplatform.enums.LogEvent;
import club.c1sec.c1ctfplatform.services.LogService;
import club.c1sec.c1ctfplatform.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginRateLimiter extends BasicRateLimiter {
    @Autowired
    LogService logService;

    Callback callback = new Callback() {
        @Override
        public void callBack(String key) {
            logService.log(LogEvent.LOG_EVENT_LOGIN_TOO_FAST, "Login too fast with ip", key);
        }
    };

    @Override
    public String getPrefix() {
        return "LOGIN_LIMITER_";
    }

    @Override
    public Integer getRatePerMin() {
        return 30;
    }

    @Override
    public Integer getInterval() {
        return null;
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
