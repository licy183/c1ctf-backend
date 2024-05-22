package club.c1sec.c1ctfplatform.vo.attachment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EditAttachmentRequest {
    @JsonProperty("attachment_id")
    Long attachmentId;

    String url;

    String flag;

    @JsonProperty("challenge_id")
    Long challengeId;
}
