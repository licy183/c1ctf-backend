package club.c1sec.c1ctfplatform.vo.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserInfo {
    private String username;

    private String email;

    @JsonProperty("student_id")
    private String studentId;

    @JsonProperty("student_name")
    private String studentName;

    @JsonProperty("is_admin")
    private Boolean isAdmin;

    @JsonProperty("is_student")
    private Boolean isStudent;

    private Boolean banned;

    private Integer rank;
}
