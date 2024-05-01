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
@Table(name = "categories")
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @JsonProperty("category_id")
    private Long categoryId;

    @Column(length = 64)
    private String name;
}
