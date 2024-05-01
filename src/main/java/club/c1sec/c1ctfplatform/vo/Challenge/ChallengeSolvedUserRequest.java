package club.c1sec.c1ctfplatform.vo.Challenge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChallengeSolvedUserRequest {
    @JsonProperty("challenge_id")
    @NotNull(message = "题目 id 不能为空")
    private Long challengeId;
}
