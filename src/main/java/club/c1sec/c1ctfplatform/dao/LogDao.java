package club.c1sec.c1ctfplatform.dao;

import club.c1sec.c1ctfplatform.enums.LogEvent;
import club.c1sec.c1ctfplatform.po.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LogDao extends JpaRepository<Log, Long> {
    Page<Log> findLogByMessageLike(String message, Pageable pageable);

    Page<Log> findLogByEvent(LogEvent logEvent, Pageable pageable);

    Page<Log> findLogByEventAndMessageLike(LogEvent logEvent, String message, Pageable pageable);
}
