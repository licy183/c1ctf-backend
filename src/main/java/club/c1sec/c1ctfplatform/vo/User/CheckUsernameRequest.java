package club.c1sec.c1ctfplatform.vo.User;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class CheckUsernameRequest {
    @NotNull(message = "用户名不能为空")
    private String username;
}
