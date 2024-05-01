package club.c1sec.c1ctfplatform.dao;

import club.c1sec.c1ctfplatform.po.Challenge;
import club.c1sec.c1ctfplatform.vo.Attachment.AttachmentChallengeInfo;
import club.c1sec.c1ctfplatform.vo.Challenge.ChallengeInfo;
import club.c1sec.c1ctfplatform.vo.Challenge.ChallengeSolvedUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ChallengeDao extends JpaRepository<Challenge, Long> {
    List<Challenge> findAll();

    Challenge findByChallengeId(Long id);

    @Query("SELECT new club.c1sec.c1ctfplatform.vo.Attachment.AttachmentChallengeInfo(chall.challengeId, atta.attachmentId, chall.challengeSeed, chall.isOpen) FROM Challenge chall INNER JOIN Attachment atta ON atta.challengeId = chall.challengeId WHERE atta.flag = :flag")
    AttachmentChallengeInfo findChallengeByFlag(String flag);

    @Query("SELECT new club.c1sec.c1ctfplatform.vo.Challenge.ChallengeInfo(chall.challengeId, chall.title, cate.name, chall.score) FROM Challenge chall LEFT JOIN Category cate ON cate.categoryId = chall.categoryId WHERE chall.isOpen = true")
    List<ChallengeInfo> findAllOpenChallengeInfo();

    Boolean existsByChallengeId(Long id);

    void deleteByChallengeId(Long id);

    @Query("SELECT new club.c1sec.c1ctfplatform.vo.Challenge.ChallengeSolvedUser(u.username, sub.submitTime) FROM Submission sub LEFT JOIN User u ON sub.submitUserId = u.userId WHERE sub.challengeId = :id AND u.banned = false AND u.userRole <> club.c1sec.c1ctfplatform.enums.UserRole.USER_ROLE_ADMIN ORDER BY sub.submitTime ASC")
    List<ChallengeSolvedUser> getSolvedUserByChallengeId(Long id, Pageable pageable);
}
