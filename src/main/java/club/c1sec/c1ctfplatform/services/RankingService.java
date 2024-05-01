package club.c1sec.c1ctfplatform.services;

import club.c1sec.c1ctfplatform.checkers.MatchOpenChecker;
import club.c1sec.c1ctfplatform.po.Challenge;
import club.c1sec.c1ctfplatform.po.Submission;
import club.c1sec.c1ctfplatform.vo.Ranking.RankChartInfo;
import club.c1sec.c1ctfplatform.vo.Ranking.RankChartPoint;
import club.c1sec.c1ctfplatform.vo.Ranking.RankingInfo;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

@Service
public class RankingService {
    @Autowired
    ChallengeService challengeService;

    @Autowired
    SubmissionService submissionService;

    @Autowired
    MatchOpenChecker matchOpenChecker;

    @Autowired
    ConfigService configService;

    @Autowired
    UserService userService;

    @Getter
    List<RankingInfo> ranking;

    @Getter
    HashMap<Long, Integer> reverseRanking;

    @Getter
    List<RankChartInfo> rankChartInfos;

    public long calcDynamicScore(int solvedCount, int baseScore, int minScore) {
        if (solvedCount <= 1) {
            return baseScore;
        }
        double score = (baseScore / 1000.0) * (-28730.0 * Math.pow(solvedCount, 0.007236) + 29840.0);
        if (score < minScore) {
            return minScore;
        } else {
            return Math.round(score);
        }
    }

    public void refreshAllDynamicScore() {
        int dynamicScoreBase = configService.getDynamicScoreBase();
        int dynamicScoreMin = configService.getDynamicScoreMin();

        List<Challenge> challenges = challengeService.getAllChallenge();
        for (Challenge challenge : challenges) {
            int solvedCount = submissionService.getSolvedCount(challenge.getChallengeId());
            if (challenge.getIsDynamicScore()) {
                long score = this.calcDynamicScore(solvedCount, dynamicScoreBase, dynamicScoreMin);
                challenge.setScore(score);
            }
            challenge.setSolvedCount(solvedCount);
            challengeService.addChallenge(challenge);
        }
    }

    public void refreshOneDynamicScore(Long challengeId) {
        int dynamicScoreBase = configService.getDynamicScoreBase();
        int dynamicScoreMin = configService.getDynamicScoreMin();

        Challenge challenge = challengeService.getChallengeById(challengeId);
        int solvedCount = submissionService.getSolvedCount(challengeId);
        challenge.setSolvedCount(solvedCount);

        if (challenge.getIsDynamicScore()) {
            long score = this.calcDynamicScore(solvedCount, dynamicScoreBase, dynamicScoreMin);
            challenge.setScore(score);
        }
        challengeService.addChallenge(challenge);
    }

    @Scheduled(fixedRate = 30000) // 30 秒刷新一次
    public void autoRefreshRanking() {
        if (!matchOpenChecker.check(null, null)) {
            return; // 比赛没开始, 不排序
        }
        this.refreshRanking();
    }

    public void refreshRanking() {
        this.refreshAllDynamicScore();

        List<Challenge> challenges = challengeService.getAllChallenge();
        HashMap<Long, Long> challenge2ScoreMap = new HashMap<>();

        for (Challenge challenge : challenges) {
            challenge2ScoreMap.put(challenge.getChallengeId(), challenge.getScore());
        }
        List<RankingInfo> tempRanking = userService.findAllRankingInfo();
        for (RankingInfo rankingInfo : tempRanking) {
            List<Submission> submissions = submissionService.getBySubmitUserId(rankingInfo.getUserId());
            long totalScore = 0;
            for (Submission submission : submissions) {
                try {
                    totalScore += challenge2ScoreMap.get(submission.getChallengeId());
                } catch (Exception ignored) {

                }
            }
            if (submissions.size() != 0) {
                rankingInfo.setLastSubmitTime(submissions.get(0).getSubmitTime().toInstant());
            }
            rankingInfo.setScore(totalScore);
        }

        tempRanking.sort(new Comparator<RankingInfo>() { // 似乎是不稳定排序, 待测试是否有效
            @Override
            public int compare(RankingInfo a, RankingInfo b) {
                if (b.getScore().equals(a.getScore())) {
                    if (a.getLastSubmitTime() == null) {
                        a.setLastSubmitTime(Instant.parse("1970-01-01T00:00:00Z"));
                    }
                    if (b.getLastSubmitTime() == null) {
                        b.setLastSubmitTime(Instant.parse("1970-01-01T00:00:00Z"));
                    }
                    if (a.getLastSubmitTime().isAfter(b.getLastSubmitTime())) {
                        return 1;
                    } else {
                        return -1;
                    }
                } else {
                    return (int) (b.getScore() - a.getScore());
                }
            }
        });

        HashMap<Long, Integer> tempReverseRanking = new HashMap<>();
        for (int i = 0; i < tempRanking.size(); i++) {
            tempReverseRanking.put(tempRanking.get(i).getUserId(), i);
        }

        List<RankChartInfo> tempRankingChartInfos = new ArrayList<>();
        for (int i = 0; i < tempRanking.size() && i < 10; i++) {
            RankChartInfo rankChartInfo = new RankChartInfo();
            List<RankChartPoint> rankChartPoints = new ArrayList<>();

            Long userId = tempRanking.get(i).getUserId();
            Long totalScore = 0L;
            rankChartInfo.setUsername(tempRanking.get(i).getUsername());
            rankChartInfo.setPoints(rankChartPoints);

            List<Submission> submissions = submissionService.getBySubmitUserId(userId);

            rankChartPoints.add(new RankChartPoint(configService.getMatchOpenTime().toEpochMilli(), 0L));

            for (int j = submissions.size() - 1; j >= 0; j--) {
                Submission sub = submissions.get(j);
                RankChartPoint rankChartPoint = new RankChartPoint();
                totalScore += challenge2ScoreMap.get(sub.getChallengeId());
                rankChartPoint.setX(sub.getSubmitTime().getTime());
                rankChartPoint.setY(totalScore);
                rankChartPoints.add(rankChartPoint);
            }

            rankChartPoints.add(new RankChartPoint(System.currentTimeMillis(), totalScore));
            tempRankingChartInfos.add(rankChartInfo);
        }

        this.ranking = tempRanking;
        this.reverseRanking = tempReverseRanking;
        this.rankChartInfos = tempRankingChartInfos;
    }
}
