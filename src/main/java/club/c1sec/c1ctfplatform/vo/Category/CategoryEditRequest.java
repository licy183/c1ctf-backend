package club.c1sec.c1ctfplatform.vo.Category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CategoryEditRequest {
    @JsonProperty("category_id")
    private Long categoryId;

    @JsonProperty("name")
    private String name;
}
