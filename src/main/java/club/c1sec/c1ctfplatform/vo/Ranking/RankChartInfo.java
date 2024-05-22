package club.c1sec.c1ctfplatform.vo.ranking;

import lombok.Data;

import java.util.List;

@Data
public class RankChartInfo {
    String username;
    List<RankChartPoint> points;
}
