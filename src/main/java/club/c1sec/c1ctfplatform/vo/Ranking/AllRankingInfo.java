package club.c1sec.c1ctfplatform.vo.Ranking;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllRankingInfo {
    @JsonProperty("ranking")
    List<RankingInfo> Ranking;

    @JsonProperty("reverse_ranking")
    Map ReverseRanking;
}
