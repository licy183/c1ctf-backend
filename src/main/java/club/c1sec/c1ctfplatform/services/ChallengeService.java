package club.c1sec.c1ctfplatform.services;

import club.c1sec.c1ctfplatform.dao.ChallengeDao;
import club.c1sec.c1ctfplatform.dao.ContaineredChallengeDao;
import club.c1sec.c1ctfplatform.po.Challenge;
import club.c1sec.c1ctfplatform.po.ContaineredChallenge;
import club.c1sec.c1ctfplatform.vo.attachment.AttachmentChallengeInfo;
import club.c1sec.c1ctfplatform.vo.challenge.ChallengeInfo;
import club.c1sec.c1ctfplatform.vo.challenge.ChallengeSolvedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Service
public class ChallengeService {
    @Autowired
    ChallengeDao challengeDao;

    @Autowired
    ContaineredChallengeDao containeredChallengeDao;

    public List<Challenge> getAllChallenge() {
        return challengeDao.findAll();
    }

    public List<ChallengeInfo> getOpenChallengeInfo() {
        return challengeDao.findAllOpenChallengeInfo();
    }

    public Challenge getChallengeById(Long id) {
        return challengeDao.findByChallengeId(id);
    }

    public AttachmentChallengeInfo getChallengeByFlag(String flag) {
        return challengeDao.findChallengeByFlag(flag);
    }

    public void addChallenge(Challenge challenge) {
        challengeDao.save(challenge);
    }

    public void deleteByChallengeId(Long id) {
        challengeDao.deleteByChallengeId(id);
    }

    public List<ChallengeSolvedUser> getSolvedUserByChallengeId(Long id) {
        Pageable pageable = PageRequest.of(0, 3);
        return challengeDao.getSolvedUserByChallengeId(id, pageable);
    }

    public long calcAttachmentNumber(long userSeed, long challengeSeed, int attachmentAmount) {
        Random random = new Random(userSeed * challengeSeed);
        int t = random.nextInt();
        t = t < 0 ? -t : t;
        return t % attachmentAmount;
    }

    public ContaineredChallenge getContaineredChallengeConfigByChallengeId(Long challengeId) {
        return containeredChallengeDao.findContaineredChallengeByChallengeId(challengeId);
    }

    public void saveContaineredChallenge(ContaineredChallenge containeredChallenge) {
        containeredChallengeDao.save(containeredChallenge);
    }

    public boolean isInvalidFlag(String flag) {
        return containeredChallengeDao.existsByFlag(flag);
    }

    public Challenge getContaineredChallengeInfoByFlagAndUser(String flag, Long userId) {
        return challengeDao.findContaineredChallengeInfoByFlagAndUser(flag, userId);
    }

}
