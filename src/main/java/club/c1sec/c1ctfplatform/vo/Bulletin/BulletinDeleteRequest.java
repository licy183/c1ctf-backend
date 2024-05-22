package club.c1sec.c1ctfplatform.vo.bulletin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class BulletinDeleteRequest {
    @NotNull
    @JsonProperty("bulletin_id")
    private Long bulletinId;
}
