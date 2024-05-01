package club.c1sec.c1ctfplatform.vo.Challenge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor
public class ChallengeSolvedUser {
    private String username;

    @JsonProperty("solved_time")
    private Date solvedTime;
}
