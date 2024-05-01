package club.c1sec.c1ctfplatform.dao;

import club.c1sec.c1ctfplatform.po.Bulletin;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface BulletinDao extends JpaRepository<Bulletin, Long> {
    List<Bulletin> findAllByOrderByPublishTimeDesc();

    Bulletin findByBulletinId(Long id);

    void deleteByBulletinId(Long id);

    @Query("SELECT bulletinId FROM Bulletin ORDER BY bulletinId DESC")
    List<Long> getLastBulletinId(Pageable pageable);
}
