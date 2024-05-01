package club.c1sec.c1ctfplatform.vo.Ranking;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;

@Data
@AllArgsConstructor
public class RankingInfo {
    public RankingInfo(Long userId, String username) {
        this.userId = userId;
        this.username = username;
        this.score = 0L;
        this.lastSubmitTime = null;
    }

    @JsonIgnore // 不返回 uid
    Long userId;

    String username;

    Long score;

    @JsonProperty("last_submit_time")
    Instant lastSubmitTime;
}
