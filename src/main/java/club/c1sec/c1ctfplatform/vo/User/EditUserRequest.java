package club.c1sec.c1ctfplatform.vo.user;

import club.c1sec.c1ctfplatform.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class EditUserRequest {
    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("user_role")
    private UserRole userRole;

    private String username;

    private String password;

    private String email;

    @JsonProperty("student_id")
    private String studentId;

    @JsonProperty("student_name")
    private String studentName;

    private Boolean banned;
}
