package club.c1sec.c1ctfplatform.po;

import club.c1sec.c1ctfplatform.enums.ContainerStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Data
@DynamicUpdate
@Table(name = "containers", indexes = {@Index(columnList = "envId")})
@AllArgsConstructor
@NoArgsConstructor
@IdClass(ContainerPK.class)
public class Container {
    @Id
    @Column
    @JsonProperty("user_id")
    private Long userId;

    @Id
    @Column
    @JsonProperty("challenge_id")
    private Long challengeId;

    @Column(unique = true, name = "envId")
    @JsonProperty("envid")
    private Long envId;

    @Enumerated
    @Column
    @JsonProperty("status")
    private ContainerStatus status;

    @Column
    @JsonProperty("host")
    private String host;

    @Column
    @JsonProperty("port")
    private Integer port;

    @Column(unique = true)
    @JsonProperty("flag")
    private String flag;

    @Column
    @JsonProperty("expire")
    private Instant expire;
}
