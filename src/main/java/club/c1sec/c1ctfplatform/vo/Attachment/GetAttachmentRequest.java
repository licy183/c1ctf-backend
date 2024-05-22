package club.c1sec.c1ctfplatform.vo.attachment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetAttachmentRequest {
    @NotNull(message = "题目 ID 不能为空")
    @JsonProperty("challenge_id")
    Long challengeId;
}
