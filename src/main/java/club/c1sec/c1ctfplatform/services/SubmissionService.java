package club.c1sec.c1ctfplatform.services;

import club.c1sec.c1ctfplatform.dao.SubmissionDao;
import club.c1sec.c1ctfplatform.po.Submission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubmissionService {
    @Autowired
    SubmissionDao submissionDao;

    @Autowired
    ChallengeService challengeService;

    @Autowired
    AttachmentService attachmentService;

    public Boolean isSubmissionExist(Long challengeId, Long submitUserId) {
        return submissionDao.existsByChallengeIdAndSubmitUserId(challengeId, submitUserId);
    }

    public void addSubmission(Submission submission) {
        submissionDao.save(submission);
    }

    /**
     * 检验用户提交的 flag 是否与随机 flag 对应
     *
     * @param challengeId
     * @param attachmentId
     * @param challengeSeed
     * @param userSeed
     * @return
     */
    public Boolean isSubmissionValid(long challengeId, long attachmentId, long challengeSeed, long userSeed) {
        int attachmentIndex = (int) challengeService.calcAttachmentNumber(challengeSeed, userSeed, attachmentService.countByChallengeId(challengeId));
        return attachmentService.findAttachmentByChallengeIdWithIndex(challengeId, attachmentIndex).getAttachmentId() == attachmentId;
    }

    public int getSolvedCount(Long challengeId) {
        return submissionDao.countByChallengeId(challengeId);
    }

    public List<Submission> getBySubmitUserId(Long userId) {
        return submissionDao.getBySubmitUserIdOrderBySubmitTimeDesc(userId);
    }
}
