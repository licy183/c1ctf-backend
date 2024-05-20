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

    private String url; // 当isContainer=true，url表示链接模板"http://{host}:{port}"

    @JsonProperty("is_container")
    private Boolean isContainer; // 是否需要申请容器

}
