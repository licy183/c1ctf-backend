package club.c1sec.c1ctfplatform.vo.Config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class MatchInfo {
    @JsonProperty("match_open_time")
    private Long openTime;

    @JsonProperty("match_end_time")
    private Long endTime;

    @JsonProperty("register_open")
    private Boolean registerOpen;
}
