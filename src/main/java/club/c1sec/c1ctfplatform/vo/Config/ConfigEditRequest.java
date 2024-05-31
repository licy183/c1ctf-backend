package club.c1sec.c1ctfplatform.vo.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
public class ConfigEditRequest {
    @JsonProperty("match_open_time")
    private Instant openTime;

    @JsonProperty("match_end_time")
    private Instant endTime;

    @JsonProperty("register_open")
    private Boolean registerOpen;

    @JsonProperty("dynamic_score_base")
    private Integer dynamicScoreBase;

    @JsonProperty("dynamic_score_min")
    private Integer dynamicScoreMin;

    @JsonProperty("container_count")
    private Integer containerCount;

    @JsonProperty("container_flag_format")
    private String containerFlagFormat;

    @JsonProperty("login_limit")
    private Boolean loginLimit;
}
