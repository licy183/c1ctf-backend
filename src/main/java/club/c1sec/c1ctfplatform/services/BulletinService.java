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
    @Autowired
    BulletinDao bulletinDao;

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
}
