package club.c1sec.c1ctfplatform.vo.Challenge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChallengeDetail {
    @JsonProperty("challenge_id")
    private Long challengeId;

    private String title;

    private String introduction;

    private Long score;

    private String url;
}
