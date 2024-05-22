package club.c1sec.c1ctfplatform.vo.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class RegisterRequest {
    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "密码不能为空")
    private String password;

    @NotNull(message = "邮件不能为空")
    private String email;

    @NotNull(message = "验证码不能为空")
    @JsonProperty("verify_code")
    private String verifyCode;

    @JsonProperty("student_id")
    private String studentId;

    @JsonProperty("student_name")
    private String studentName;

    @NotNull(message = "选项不能为空")
    @JsonProperty("is_student")
    private Boolean isStudent;
}
