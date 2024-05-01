package club.c1sec.c1ctfplatform.services;

import club.c1sec.c1ctfplatform.dao.LogDao;
import club.c1sec.c1ctfplatform.enums.LogEvent;
import club.c1sec.c1ctfplatform.po.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class LogService {
    @Autowired
    LogDao logDao;

    public void log(LogEvent event, String... msg) {
        String finalMsg = String.join(" | ", msg);
        Log log = new Log();
        log.setLogTime(new Date());
        log.setEvent(event);
        log.setMessage(finalMsg);
        logDao.save(log);
    }

    public Page<Log> getLogWithPage(int pageNo, int pageSize, LogEvent logType, String content) {
        Pageable pageable;
        Page<Log> page;
        pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, "logTime"));

        if (content != null) {
            content = content.replace("%", "\\%");
            content = content.replace("_", "\\_");

            content = "%" + content + "%";
        }

        if (logType == null && content == null) {
            page = logDao.findAll(pageable);
        } else if (logType == null) {
            page = logDao.findLogByMessageLike(content, pageable);
        } else if (content == null) {
            page = logDao.findLogByEvent(logType, pageable);
        } else {
            page = logDao.findLogByEventAndMessageLike(logType, content, pageable);
        }
        return page;
    }
}
