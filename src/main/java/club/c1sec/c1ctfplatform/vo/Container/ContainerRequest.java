package club.c1sec.c1ctfplatform.vo.container;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ContainerRequest {
    @NotNull(message = "题目id不能为空")
    @JsonProperty("challenge_id")
    private Long challengeId;
}

