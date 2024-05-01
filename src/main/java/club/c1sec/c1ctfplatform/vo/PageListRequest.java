package club.c1sec.c1ctfplatform.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PageListRequest {
    @JsonProperty("page_no")
    private Integer pageNo = 0;

    @JsonProperty("page_size")
    private Integer pageSize = 30;
}
