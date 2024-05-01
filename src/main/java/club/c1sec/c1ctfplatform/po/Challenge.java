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
@Table(name = "challenges")
@AllArgsConstructor
@NoArgsConstructor
public class Challenge {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @JsonProperty("challenge_id")
    private Long challengeId;

    @Column
    @JsonProperty("category_id")
    private Long categoryId;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Column
    private Long score;

    @Column
    @JsonProperty("solved_count")
    private Integer solvedCount;

    @Column
    @JsonProperty("challenge_seed")
    private Long challengeSeed;

    @Column
    @JsonProperty("is_dynamic_score")
    private Boolean isDynamicScore;

    @Column
    @JsonProperty("is_open")
    private Boolean isOpen;
}
