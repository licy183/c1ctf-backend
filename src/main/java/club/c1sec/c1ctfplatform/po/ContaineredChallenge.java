package club.c1sec.c1ctfplatform.po;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Data
@DynamicUpdate
@Table(name = "containered_challenge")
@AllArgsConstructor
@NoArgsConstructor
public class ContaineredChallenge {
    @Id
    @JsonProperty("challenge_id")
    private Long challengeId;

    @Column
    @JsonProperty("compose_file")
    private String composeFile;

    @Column
    @JsonProperty("is_generate_flag")
    private Boolean isGenerateFlag;

    @Column
    @JsonProperty("flag")
    private String flag;

    // %host% %port%会被替换
    @Column
    @JsonProperty("url_template")
    private String urlTemplate;
}
