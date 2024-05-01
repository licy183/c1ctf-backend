package club.c1sec.c1ctfplatform.vo.Challenge;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChallengeInfo {
    @JsonProperty("challenge_id")
    private Long challengeId;

    private String title;

    @JsonIgnore
    private String categoryName;

    private Long score;
}
