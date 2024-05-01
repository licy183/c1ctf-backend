package club.c1sec.c1ctfplatform.vo.Log;

import club.c1sec.c1ctfplatform.enums.LogEvent;
import club.c1sec.c1ctfplatform.vo.PageListRequest;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class GetLogRequest {
    @NotNull(message = "分页请求不能为空")
    @JsonProperty("page")
    private PageListRequest pageListRequest;

    @JsonProperty("log_type")
    private LogEvent logType;

    @JsonProperty("content")
    private String content;
}
