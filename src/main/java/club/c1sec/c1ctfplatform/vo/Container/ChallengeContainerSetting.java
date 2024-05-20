package club.c1sec.c1ctfplatform.vo.Container;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ChallengeContainerSetting {
    @JsonProperty("challenge_id")
    private Long challengeId;

    @JsonProperty("compose_file")
    private String composeFile;

    @JsonProperty("is_generate_flag")
    private Boolean isGenerateFlag;

    // %host% %port%会被替换
    @JsonProperty("url_template")
    private String urlTemplate;
}
