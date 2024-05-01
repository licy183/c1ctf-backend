package club.c1sec.c1ctfplatform.dao;

import club.c1sec.c1ctfplatform.po.User;
import club.c1sec.c1ctfplatform.vo.Ranking.RankingInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    @Query("SELECT new club.c1sec.c1ctfplatform.vo.Ranking.RankingInfo(u.userId, u.username) FROM User u WHERE u.banned = false AND u.userRole <> club.c1sec.c1ctfplatform.enums.UserRole.USER_ROLE_ADMIN")
    List<RankingInfo> findAllRankingInfo();

    List<User> findAll();

    User findUserByUserId(Long userId);

    User findUserByUsername(String username);

    User findUserByEmail(String username);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);

    Boolean existsByStudentId(String studentId);
}
