package club.c1sec.c1ctfplatform.dao;


import club.c1sec.c1ctfplatform.po.ContaineredChallenge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContaineredChallengeDao extends JpaRepository<ContaineredChallenge, Long> {

    ContaineredChallenge findContaineredChallengeByChallengeId(Long challengeId);

    Boolean existsByFlag(String flag);

}
