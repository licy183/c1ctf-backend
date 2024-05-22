package club.c1sec.c1ctfplatform.vo.bulletin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
public class BulletinEditRequest {
    @JsonProperty("bulletin_id")
    private Long bulletinId;

    @JsonProperty("title")
    private String title;

    @JsonProperty("content")
    private String content;

    @JsonProperty("publish_time")
    private Instant publishTime;

    @JsonProperty("is_sticky")
    private Boolean isSticky;
}
