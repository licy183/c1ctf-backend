package club.c1sec.c1ctfplatform.vo.submission;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class SubmitFlagRequest {
    @NotNull(message = "flag 不能为空")
    private String flag;
}
