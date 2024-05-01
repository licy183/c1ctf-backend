package club.c1sec.c1ctfplatform.vo.Category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CategoryDeleteRequest {
    @JsonProperty("category_id")
    private Long categoryId;
}
