package club.c1sec.c1ctfplatform.vo.Submission;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetUserSubmitRequest {
    @NotNull(message = "需要用户 id")
    @JsonProperty("user_id")
    private Long userId;
}
