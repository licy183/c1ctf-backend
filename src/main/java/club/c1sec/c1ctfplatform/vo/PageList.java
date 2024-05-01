package club.c1sec.c1ctfplatform.vo;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PageList<T> {
    private T content;

    @JsonProperty("total_pages")
    private Integer totalPages;
}
