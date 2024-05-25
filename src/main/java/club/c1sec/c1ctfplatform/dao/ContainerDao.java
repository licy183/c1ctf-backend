package club.c1sec.c1ctfplatform.dao;

import club.c1sec.c1ctfplatform.po.Container;
import club.c1sec.c1ctfplatform.po.ContainerPK;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContainerDao extends JpaRepository<Container, ContainerPK> {
    Container findContainerByChallengeIdAndUserId(Long challengeId, Long userId);

    Long countContainerByUserId(Long userId);

    @Query("SELECT envId FROM Container where userId = :userId and status = club.c1sec.c1ctfplatform.enums.ContainerStatus.CREATED")
    List<Long> getContainerIdCreatedByUserId(Long userId);

    @Query("SELECT DISTINCT challengeId FROM Container WHERE userId = :userId and status = club.c1sec.c1ctfplatform.enums.ContainerStatus.CREATED")
    List<Long> getAllChallengeIdCreated(Long userId);

    @Query("SELECT DISTINCT challengeId FROM Container WHERE userId = :userId and status <> club.c1sec.c1ctfplatform.enums.ContainerStatus.DELETED")
    List<Long> getAllChallengeIdNotDeleted(Long userId);

    Container findContainerByFlag(String flag);

}
