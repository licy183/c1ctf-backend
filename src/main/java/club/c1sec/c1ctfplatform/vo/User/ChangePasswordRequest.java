package club.c1sec.c1ctfplatform.vo.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class ChangePasswordRequest {
    @NotNull(message = "旧密码不能为空")
    @JsonProperty("old_password")
    private String oldPassword;

    @NotNull(message = "新密码不能为空")
    @JsonProperty("new_password")
    private String newPassword;
}
