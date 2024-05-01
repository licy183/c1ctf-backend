package club.c1sec.c1ctfplatform.vo.Challenge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChallengeDetailRequest {
    @JsonProperty("challenge_id")
    private Long challengeId;
}
