package club.c1sec.c1ctfplatform.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum LogEvent {
    LOG_EVENT_SUBMIT_SUCCESS(0),                       // 提交正确的flag
    LOG_EVENT_SUBMIT_ERROR(1),                         // 提交无效的flag
    LOG_EVENT_SUBMIT_REPEAT(2),                        // 重复提交
    LOG_EVENT_SUBMIT_OTHER_USER(3),                    // 提交其他用户的flag
    LOG_EVENT_SUBMIT_REPEAT_OTHER_USER(4),             // 重复提交其他用户的flag
    LOG_EVENT_SUBMIT_TOO_FAST(5),                      // 提交flag过快
    LOG_EVENT_LOGIN_TOO_FAST(6);                       // 尝试登录过快

    int id;

    LogEvent(int id) {
        this.id = id;
    }

    @JsonValue
    int getId() {
        return id;
    }

    @JsonCreator
    static LogEvent fromId(int id) {
        return Stream.of(LogEvent.values()).filter(event -> event.id == id).findFirst().orElse(null);
    }

    public static LogEvent int2LogEvent(Integer i) {
        if (i == null) {
            return null;
        }
        switch (i) {
            case 0:
                return LOG_EVENT_SUBMIT_SUCCESS;
            case 1:
                return LOG_EVENT_SUBMIT_ERROR;
            case 2:
                return LOG_EVENT_SUBMIT_REPEAT;
            case 3:
                return LOG_EVENT_SUBMIT_OTHER_USER;
            case 4:
                return LOG_EVENT_SUBMIT_REPEAT_OTHER_USER;
            case 5:
                return LOG_EVENT_SUBMIT_TOO_FAST;
            case 6:
                return LOG_EVENT_LOGIN_TOO_FAST;
            default:
                return null;
        }
    }
}
