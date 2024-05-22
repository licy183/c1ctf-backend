package club.c1sec.c1ctfplatform.services;

import club.c1sec.c1ctfplatform.dao.ContainerDao;
import club.c1sec.c1ctfplatform.po.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContainerService {

    @Autowired
    ContainerDao containerDao;

    public Container getContainerIdByPK(Long userId, Long challengeId) {
        return containerDao.findContainerByChallengeIdAndUserId(challengeId, userId);
    }

    public Long countContainerByUserId(Long userId) {
        return (long) containerDao.getContainerIdCreatedByUserId(userId).size();
    }

    public List<Long> getAllChallengeIdCreated(Long userId) {
        return containerDao.getAllChallengeIdCreated(userId);
    }

    public List<Long> getAllChallengeIdNotDeleted(Long userId) {
        return containerDao.getAllChallengeIdNotDeleted(userId);
    }

    public void saveContainer(Container container) {
        containerDao.save(container);
    }

}
