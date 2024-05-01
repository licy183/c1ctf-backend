package club.c1sec.c1ctfplatform.vo.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginInfo {
    private String username;

    private String token;

    @JsonProperty("is_admin")
    private Boolean isAdmin;
}
