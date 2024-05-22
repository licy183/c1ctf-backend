package club.c1sec.c1ctfplatform.vo.container;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChallengeContainerSetting {
    @NotNull(message = "题目id不能为空")
    @JsonProperty("challenge_id")
    private Long challengeId;

    @JsonProperty("compose_file")
    private String composeFile;

    @JsonProperty("is_generate_flag")
    private Boolean isGenerateFlag;

    // %host% %port%会被替换
    @JsonProperty("url_template")
    private String urlTemplate;

    @JsonProperty("flag")
    private String flag;
}
