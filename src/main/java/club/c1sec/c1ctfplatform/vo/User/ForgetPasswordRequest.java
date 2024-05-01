package club.c1sec.c1ctfplatform.vo.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ForgetPasswordRequest {
    @NotNull(message = "邮箱不能为空")
    private String email;

    @NotNull(message = "验证码不能为空")
    @JsonProperty("verify_code")
    private String verifyCode;

    @NotNull(message = "新密码不能为空")
    @JsonProperty("new_password")
    private String newPassword;

    @NotNull(message = "用户名不能为空")
    private String username;
}
