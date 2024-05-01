package club.c1sec.c1ctfplatform.limiter;

import club.c1sec.c1ctfplatform.enums.LogEvent;
import club.c1sec.c1ctfplatform.services.LogService;
import club.c1sec.c1ctfplatform.services.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SubmitRateLimiter extends BasicRateLimiter {
    @Autowired
    LogService logService;

    Callback callback = new Callback() {
        @Override
        public void callBack(String key) {
            logService.log(LogEvent.LOG_EVENT_SUBMIT_TOO_FAST, "Submit flag too fast with username", key);
        }
    };

    @Override
    public String getPrefix() {
        return "SUBMIT_LIMITER_";
    }

    @Override
    public Integer getRatePerMin() {
        return 20;
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
