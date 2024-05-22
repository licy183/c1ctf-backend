package club.c1sec.c1ctfplatform.vo.ranking;

import club.c1sec.c1ctfplatform.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class RankingInfo {
    public RankingInfo(Long userId, String username, UserRole userRole) {
        this.userId = userId;
        this.username = username;
        this.userRole = userRole;
        this.score = 0L;
        this.lastSubmitTime = null;
    }

    @JsonIgnore // 不返回 uid
    Long userId;

    @JsonIgnore // 不返回 urole
    UserRole userRole;

    String username;

    Long score;

    @JsonProperty("last_submit_time")
    Instant lastSubmitTime;
}
