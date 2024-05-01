package club.c1sec.c1ctfplatform.vo.Challenge;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class ChallengeSolvedUsersWithCount {
    @JsonProperty("solved_users")
    List<ChallengeSolvedUser> solvedUsers;

    @JsonProperty("solved_count")
    Integer solvedCount;
}
