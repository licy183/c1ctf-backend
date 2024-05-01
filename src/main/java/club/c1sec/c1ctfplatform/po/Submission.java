package club.c1sec.c1ctfplatform.po;

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
@Table(name = "submissions", indexes = {@Index(columnList = "challengeId"), @Index(columnList = "submitUserId")}, uniqueConstraints = {@UniqueConstraint(columnNames = {"challengeId", "submitUserId"})})
@AllArgsConstructor
@NoArgsConstructor
public class Submission {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @JsonProperty("submission_id")
    private Long submissionId;

    @Column(name = "challengeId")
    @JsonProperty("challenge_id")
    private Long challengeId;

    @Column
    @JsonProperty("submit_time")
    private Date submitTime;

    @Column(name = "submitUserId")
    @JsonProperty("submit_user_id")
    private Long submitUserId;
}
