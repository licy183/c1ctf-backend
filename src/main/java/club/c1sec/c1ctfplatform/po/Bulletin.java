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
@Table(name = "bulletins")
@AllArgsConstructor
@NoArgsConstructor
public class Bulletin {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @JsonProperty("bulletin_id")
    private Long bulletinId;

    @Column(columnDefinition = "TEXT")
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column
    @JsonProperty("publish_time")
    private Date publishTime;

    @Column
    @JsonProperty("is_sticky")
    private Boolean isSticky;
}
