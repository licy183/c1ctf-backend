package club.c1sec.c1ctfplatform.vo.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CasLoginResult {
    @JsonProperty("need_register")
    private boolean needRegister;

    private String token;

    @JsonProperty("register_uuid")
    private String uuid;

    @JsonProperty("student_id")
    private String studentId;

    @JsonProperty("student_name")
    private String studentName;
}
