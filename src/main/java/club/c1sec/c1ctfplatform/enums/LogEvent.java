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
    LOG_EVENT_LOGIN_TOO_FAST(6),                       // 尝试登录过快
    LOG_EVENT_CONTAINER_CREATE(7),                     // 申请容器
    LOG_EVENT_CONTAINER_DELETE(8),                     // 删除容器
    LOG_EVENT_CONTAINER_RENEW(9),                      // 续期容器
    LOG_EVENT_CONTAINER_MAX(10),                       // 容器数量达到最大值
    LOG_EVENT_CONTAINER_CREATE_REPEAT(11),             // 申请已经申请过容器的题目
    LOG_EVENT_CONTAINER_UNMATCHED_TYPE(12),            // 操作不需要申请容器的题目
    LOG_EVENT_CONTAINER_INVALID(13),                   // 操作没有申请过的容器
    LOG_EVENT_CONTAINER_ERROR(14),                     // 容器处理过程中出错
    LOG_EVENT_CONTAINER_QUERY(15),                     // 查询容器信息
    LOG_EVENT_GET_CHALLENGE_DETAIL(16);                // 获取题目详情

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
            case 7:
                return LOG_EVENT_CONTAINER_CREATE;
            case 8:
                return LOG_EVENT_CONTAINER_DELETE;
            case 9:
                return LOG_EVENT_CONTAINER_RENEW;
            case 10:
                return LOG_EVENT_CONTAINER_MAX;
            case 11:
                return LOG_EVENT_CONTAINER_CREATE_REPEAT;
            case 12:
                return LOG_EVENT_CONTAINER_UNMATCHED_TYPE;
            case 13:
                return LOG_EVENT_CONTAINER_INVALID;
            case 14:
                return LOG_EVENT_CONTAINER_ERROR;
            case 15:
                return LOG_EVENT_CONTAINER_QUERY;
            case 16:
                return LOG_EVENT_GET_CHALLENGE_DETAIL;
            default:
                return null;
        }
    }
}
