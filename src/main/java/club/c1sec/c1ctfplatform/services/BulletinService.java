package club.c1sec.c1ctfplatform.services;

import club.c1sec.c1ctfplatform.dao.BulletinDao;
import club.c1sec.c1ctfplatform.po.Bulletin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BulletinService {
    private static final String REDIS_PREFIX_LAST_READ_TIME = "LAST_READ_TIME_";
    private static final String REDIS_KEY_LAST_POST_TIME = "LAST_POST_TIME";

    private final BulletinDao bulletinDao;

    private final RedisService redisService;

    @Autowired
    public BulletinService(BulletinDao bulletinDao, RedisService redisService) {
        this.bulletinDao = bulletinDao;
        this.redisService = redisService;
        setLatestPostTime();
    }

    public List<Bulletin> findAll() {
        return bulletinDao.findAllByOrderByPublishTimeDesc();
    }

    public Bulletin findByBulletinId(Long id) {
        return bulletinDao.findByBulletinId(id);
    }

    public void deleteByBulletinId(Long id) {
        bulletinDao.deleteByBulletinId(id);
    }

    public void addBulletin(Bulletin bulletin) {
        bulletinDao.save(bulletin);
        setLatestPostTime();
    }

    public Long getLastBulletinId() {
        Pageable pageable = PageRequest.of(0, 1);
        List<Long> ids = bulletinDao.getLastBulletinId(pageable);
        if (ids.size() == 0) {
            return null;
        } else {
            return ids.get(0);
        }
    }

    public void setLatestPostTime() {
        redisService.setKeyValue(REDIS_KEY_LAST_POST_TIME, String.valueOf(System.currentTimeMillis()));
    }

    public Long getLatestPostTime() {
        final String longValueStr = redisService.getValue(REDIS_KEY_LAST_POST_TIME);
        return longValueStr == null ? null : Long.valueOf(longValueStr);
    }

    public void setLastReadTime(Long userId) {
        redisService.setKeyValue(REDIS_PREFIX_LAST_READ_TIME + userId, String.valueOf(System.currentTimeMillis()));
    }

    public Long getLastReadTime(Long userId) {
        final String longValueStr = redisService.getValue(REDIS_PREFIX_LAST_READ_TIME + userId);
        return longValueStr == null ? null : Long.valueOf(longValueStr);
    }

    public boolean haveUnreadBulletin(Long userId) {
        Long latestPostTime = getLatestPostTime();
        Long lastReadTime = getLastReadTime(userId);
        if (lastReadTime != null) {
            return lastReadTime < latestPostTime;
        }
        return false;
    }
}
