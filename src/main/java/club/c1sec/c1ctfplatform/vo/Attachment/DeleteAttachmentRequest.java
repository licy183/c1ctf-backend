package club.c1sec.c1ctfplatform.vo.Attachment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class DeleteAttachmentRequest {
    @NotNull
    @JsonProperty("attachment_id")
    Long attachmentId;
}
