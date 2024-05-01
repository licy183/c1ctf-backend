package club.c1sec.c1ctfplatform.vo.User;

import lombok.Data;
import org.hibernate.validator.constraints.Range;

import javax.validation.constraints.NotNull;

@Data
public class SendRegisterMailRequest {
    @NotNull(message = "邮箱不能为空")
    private String email;

    @Range(min = 0, max = 1, message = "无效的类型")
    @NotNull(message = "类型不能为空")
    private Integer type;
}
