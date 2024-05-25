package club.c1sec.c1ctfplatform.vo.user;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class CasRegisterRequest {
    @NotBlank
    private String uuid;
    @NotBlank(message = "用户名不能为空")
    private String username;
    @NotBlank(message = "密码不能为空")
    private String password;
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "不合法的 Email")
    private String email;
}
