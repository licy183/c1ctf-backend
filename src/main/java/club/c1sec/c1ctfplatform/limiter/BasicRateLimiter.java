package club.c1sec.c1ctfplatform.limiter;

import club.c1sec.c1ctfplatform.services.RedisService;

public class BasicRateLimiter {
    protected RedisService redisService;

    public String getPrefix() {
        return "BASIC_LIMITER_";
    }

    public Integer getRatePerMin() {
        return 10;
    }

    public Integer getInterval() {
        return 10;
    }

    public Callback getCallback() {
        return null;
    }

    public Boolean checkInterval(String key) {
        Integer interval = getInterval();
        if (interval == null) {
            return true;
        }
        String redisKey = getPrefix() + "INTERVAL_" + key;

        if (redisService.isKeyExist(redisKey)) {
            return false;
        } else {
            redisService.setKeyValueWithExpire(redisKey, "1", interval);
            return true;
        }
    }

    public Boolean checkRate(String key) {
        Integer ratePerMin = getRatePerMin();
        if (ratePerMin == null) {
            return true;
        }

        long timestamp = System.currentTimeMillis() / 1000;
        timestamp = timestamp - timestamp % 60;
        String redisKey = getPrefix() + "RATE_" + timestamp + "_" + key;
        if (redisService.isKeyExist(redisKey)) {
            int count = Integer.parseInt(redisService.getValue(redisKey));
            if (count >= ratePerMin) {
                Callback callback = getCallback();
                if (callback != null) {
                    callback.callBack(key);
                }
                return false;
            } else {
                redisService.setKeyValueWithExpire(redisKey, String.valueOf(count + 1), redisService.getRemainExpire(redisKey).intValue());
                return true;
            }
        } else {
            redisService.setKeyValueWithExpire(redisKey, "1", 60);
            return true;
        }
    }

    public Boolean check(String key) {
        return this.checkInterval(key) && this.checkRate(key);
    }
}
