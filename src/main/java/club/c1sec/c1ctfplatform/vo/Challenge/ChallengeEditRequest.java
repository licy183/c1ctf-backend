package club.c1sec.c1ctfplatform.vo.challenge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChallengeEditRequest {
    @JsonProperty("challenge_id")
    private Long challengeId;

    @JsonProperty("category_id")
    private Long categoryId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("introduction")
    private String introduction;

    @JsonProperty("is_dynamic_score")
    private Boolean isDynamicScore;

    @JsonProperty("is_open")
    private Boolean isOpen;

    @JsonProperty("score")
    private Long score;

    @JsonProperty("is_container")
    private Boolean isContainer;
}
