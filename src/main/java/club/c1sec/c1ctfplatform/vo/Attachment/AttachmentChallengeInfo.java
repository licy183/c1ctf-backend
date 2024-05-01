package club.c1sec.c1ctfplatform.vo.Attachment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AttachmentChallengeInfo {
    @JsonProperty("challenge_id")
    private Long challengeId;

    @JsonProperty("attachment_id")
    private Long attachmentId;

    @JsonProperty("challenge_seed")
    private Long challengeSeed;

    @JsonProperty("challenge_is_open")
    private boolean challengeIsOpen;
}
