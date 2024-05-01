package club.c1sec.c1ctfplatform.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Data
@DynamicUpdate
@Table(name = "attachments", indexes = {@Index(columnList = "challengeId")})
@AllArgsConstructor
@NoArgsConstructor
public class Attachment {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @JsonProperty("attachment_id")
    private Long attachmentId;

    @Column(name = "challengeId")
    @JsonProperty("challenge_id")
    private Long challengeId;

    @Column(columnDefinition = "TEXT")
    private String url;

    @Column(unique = true)
    private String flag;
}
