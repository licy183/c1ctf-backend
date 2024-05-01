package club.c1sec.c1ctfplatform.po;

import club.c1sec.c1ctfplatform.enums.LogEvent;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@Table(name = "logs")
@AllArgsConstructor
@NoArgsConstructor
public class Log {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Enumerated
    @Column
    private LogEvent event;

    @Column(columnDefinition = "TEXT")
    private String message;

    @Column
    @JsonProperty("log_time")
    private Date logTime;
}
