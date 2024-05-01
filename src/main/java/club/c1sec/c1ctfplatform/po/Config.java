package club.c1sec.c1ctfplatform.po;

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
@Table(name = "configs")
@AllArgsConstructor
@NoArgsConstructor
public class Config {
    @Id
    @Column(name = "_key", unique = true, length = 128)
    private String key;

    @Column(name = "_value", columnDefinition = "TEXT")
    private String value;
}
