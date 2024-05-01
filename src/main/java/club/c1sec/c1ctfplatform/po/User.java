package club.c1sec.c1ctfplatform.po;

import club.c1sec.c1ctfplatform.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@DynamicUpdate
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @JsonProperty("user_id")
    private Long userId;

    @Enumerated
    @Column
    @JsonProperty("user_role")
    private UserRole userRole;

    @Column(length = 64, unique = true)
    private String username;

    @Column(length = 128)
    @JsonIgnore
    private String password;

    @Column(length = 64, unique = true)
    private String email;

    @Column(unique = true, nullable = true)
    @JsonProperty("student_id")
    private String studentId;

    @Column(length = 64, nullable = true)
    @JsonProperty("student_name")
    private String studentName;

    @Column
    @JsonProperty("last_login_time")
    private Date lastLoginTime;

    @Column
    private Boolean banned;

    @Column
    @JsonProperty("user_seed")
    private Integer userSeed;
}
