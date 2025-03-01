package club.c1sec.c1ctfplatform.vo.user;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class LoginRequest {
    @NotNull(message = "用户名不能为空")
    private String username;

    @NotNull(message = "密码不能为空")
    private String password;
}
