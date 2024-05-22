package club.c1sec.c1ctfplatform.vo.challenge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteChallengeRequest {
    @NotNull(message = "题目 ID 不能为空")
    @JsonProperty("challenge_id")
    Long challengeId;
}
