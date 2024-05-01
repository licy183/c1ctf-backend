package club.c1sec.c1ctfplatform.controllers;

import club.c1sec.c1ctfplatform.checkers.AdminChecker;
import club.c1sec.c1ctfplatform.checkers.LoginChecker;
import club.c1sec.c1ctfplatform.interceptor.InterceptCheck;
import club.c1sec.c1ctfplatform.services.AuthService;
import club.c1sec.c1ctfplatform.services.RankingService;
import club.c1sec.c1ctfplatform.vo.Ranking.AllRankingInfo;
import club.c1sec.c1ctfplatform.vo.Ranking.RankChartInfo;
import club.c1sec.c1ctfplatform.vo.Ranking.RankingInfo;
import club.c1sec.c1ctfplatform.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rank")
public class RankingController {
    @Autowired
    RankingService rankingService;

    @Autowired
    AuthService authService;

    @GetMapping("/get_ranking_list")
    public Response<List<RankingInfo>> getRank() {
        Response<List<RankingInfo>> response = new Response<>();

        List<RankingInfo> ranking = rankingService.getRanking();
        int end = 30;
        if (end > ranking.size()) {
            end = ranking.size();
        }
        ranking.subList(0, end);
        response.success("", ranking);
        return response;
    }

    @InterceptCheck(checkers = {LoginChecker.class})
    @GetMapping("/get_my_ranking")
    public Response<Integer> getMyRanking() {
        Response<Integer> response = new Response<>();
        Long uid = authService.getCurrUser().getUserId();
        Integer ranking = rankingService.getReverseRanking().get(uid);
        response.success("", ranking);
        return response;
    }

    @InterceptCheck(checkers = {AdminChecker.class})
    @GetMapping("/get_all_ranking") // 获取全部排名的方式是通过获得全部 user 后, 在这个 ReverseMap 找到所有排名
    public Response<AllRankingInfo> getAllRanking() {
        Response<AllRankingInfo> response = new Response<>();
        AllRankingInfo ranking = new AllRankingInfo();
        ranking.setReverseRanking(rankingService.getReverseRanking());  //{[uid: rank]}
        ranking.setRanking(rankingService.getRanking());

        response.success("", ranking);
        return response;
    }

    @InterceptCheck(checkers = {AdminChecker.class})
    @GetMapping("/refresh_ranking")
    public Response<String> refreshRanking() {
        Response<String> response = new Response<>();
        this.rankingService.refreshRanking();
        response.success("");
        return response;
    }

    @GetMapping("/get_ranking_chart")
    public Response<List<RankChartInfo>> getRankingChart() {
        Response<List<RankChartInfo>> response = new Response<>();
        response.success("", rankingService.getRankChartInfos());
        return response;
    }
}

