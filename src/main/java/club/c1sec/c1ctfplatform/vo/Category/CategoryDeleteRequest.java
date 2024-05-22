package club.c1sec.c1ctfplatform.vo.category;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CategoryDeleteRequest {
    @NotNull
    @JsonProperty("category_id")
    private Long categoryId;
}
