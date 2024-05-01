package club.c1sec.c1ctfplatform.dao;

import club.c1sec.c1ctfplatform.po.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubmissionDao extends JpaRepository<Submission, Long> {
    Submission findBySubmissionId(Long submissionId);

    boolean existsByChallengeIdAndSubmitUserId(Long challengeId, Long submitUserId);

    @Query("SELECT COUNT(DISTINCT sub.submissionId) FROM Submission sub LEFT JOIN User u ON sub.submitUserId = u.userId WHERE u.banned = false AND sub.challengeId = :challengeId AND u.userRole <> club.c1sec.c1ctfplatform.enums.UserRole.USER_ROLE_ADMIN")
    int countByChallengeId(Long challengeId);

    List<Submission> getBySubmitUserIdOrderBySubmitTimeDesc(Long userId);
}
