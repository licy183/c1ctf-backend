package club.c1sec.c1ctfplatform.controllers;

import club.c1sec.c1ctfplatform.checkers.AdminChecker;
import club.c1sec.c1ctfplatform.checkers.LoginChecker;
import club.c1sec.c1ctfplatform.checkers.MatchStartedChecker;
import club.c1sec.c1ctfplatform.enums.LogEvent;
import club.c1sec.c1ctfplatform.interceptor.InterceptCheck;
import club.c1sec.c1ctfplatform.po.*;
import club.c1sec.c1ctfplatform.services.*;
import club.c1sec.c1ctfplatform.utils.RandomUtil;
import club.c1sec.c1ctfplatform.vo.challenge.*;
import club.c1sec.c1ctfplatform.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@InterceptCheck(checkers = {LoginChecker.class})
@RestController
@RequestMapping("/api/challenge")
public class ChallengeController {
    @Autowired
    ChallengeService challengeService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    AuthService authService;

    @Autowired
    AttachmentService attachmentService;

    @Autowired
    SubmissionService submissionService;

    @Autowired
    LogService logService;

    @InterceptCheck(checkers = {MatchStartedChecker.class})
    @GetMapping("/get_open_challenge")
    public Response<Map<String, List<ChallengeInfo>>> getOpenChallenge() {
        HashMap<String, List<ChallengeInfo>> map = new HashMap<>();
        Response<Map<String, List<ChallengeInfo>>> response = new Response<>();
        List<Category> categories = categoryService.findAllCategory();
        List<ChallengeInfo> challengeInfos = challengeService.getOpenChallengeInfo();

        for (Category category : categories) {
            map.put(category.getName(), new ArrayList<>());
        }
        for (ChallengeInfo challengeInfo : challengeInfos) {
            map.get(challengeInfo.getCategoryName()).add(challengeInfo);
        }
        response.success("", map);
        return response;
    }

    @InterceptCheck(checkers = {MatchStartedChecker.class})
    @PostMapping("/get_challenge_detail")
    public Response<ChallengeDetail> getChallengeDetail(@RequestBody ChallengeDetailRequest req) {
        Response<ChallengeDetail> response = new Response<>();
        Long challengeId = req.getChallengeId();

        if (challengeId != null) {
            Challenge challenge = challengeService.getChallengeById(challengeId);
            ChallengeDetail challengeDetail = new ChallengeDetail();
            User user = authService.getCurrUser();

            if (challenge != null && challenge.getIsOpen()) {
                int attachmentAmount = attachmentService.countByChallengeId(challengeId);
                if (attachmentAmount >= 1) {
                    long challengeSeed = challenge.getChallengeSeed();
                    long userSeed = user.getUserSeed();
                    int currChallenge = (int) challengeService.calcAttachmentNumber(userSeed, challengeSeed, attachmentAmount);
                    Attachment attachment = attachmentService.findAttachmentByChallengeIdWithIndex(challengeId, currChallenge);
                    if (attachment != null) {
                        challengeDetail.setUrl(attachment.getUrl());
                    } else {
                        challengeDetail.setUrl("");
                    }
                }

                challengeDetail.setChallengeId(challengeId);
                challengeDetail.setIntroduction(challenge.getIntroduction());
                challengeDetail.setScore(challenge.getScore());
                challengeDetail.setTitle(challenge.getTitle());
                challengeDetail.setIsContainer(challenge.getIsContainer());
                logService.log(LogEvent.LOG_EVENT_GET_CHALLENGE_DETAIL, user.getUsername());
                response.success("", challengeDetail);
            } else {
                response.fail("请求的题目不存在");
            }
        } else {
            response.invalid("无效参数");
        }
        return response;
    }

    @InterceptCheck(checkers = {AdminChecker.class})
    @GetMapping("/get_all_challenge")
    public Response<List<Challenge>> getAllChallenge() {
        Response<List<Challenge>> response = new Response<>();
        response.success("", challengeService.getAllChallenge());
        return response;
    }

    @InterceptCheck(checkers = {AdminChecker.class})
    @PostMapping("/edit_challenge")
    public Response<Long> editChallenge(@RequestBody ChallengeEditRequest req) {
        Response<Long> response = new Response<>();
        Challenge challenge;
        if (req.getChallengeId() != null) {
            Long challengeId = req.getChallengeId();
            challenge = challengeService.getChallengeById(challengeId);
            if (challenge == null) {
                response.fail("此题目 ID 不存在");
                return response;
            }
        } else {
            challenge = new Challenge();
            challenge.setChallengeSeed((long) RandomUtil.getRandomPrime());
        }

        if (req.getCategoryId() != null) {
            Long categoryId = req.getCategoryId();
            if (categoryService.existsByCategoryId(categoryId)) {
                challenge.setCategoryId(categoryId);
            } else {
                response.fail("此分类 ID 不存在");
                return response;
            }
        }
        if (req.getTitle() != null) {
            challenge.setTitle(req.getTitle());
        }
        if (req.getIntroduction() != null) {
            challenge.setIntroduction(req.getIntroduction());
        }
        if (req.getIsDynamicScore() != null) {
            challenge.setIsDynamicScore(req.getIsDynamicScore());
        }
        if (req.getIsOpen() != null) {
            challenge.setIsOpen(req.getIsOpen());
        }
        if (req.getScore() != null) {
            if (challenge.getIsDynamicScore()) {
                response.fail("动态分数题目无法设置分数");
                return response;
            } else {
                challenge.setScore(req.getScore());
            }
        }
        if (req.getIsContainer() != null) {
            challenge.setIsContainer(req.getIsContainer());
        }
        challengeService.addChallenge(challenge);
        response.success("成功", challenge.getChallengeId());
        return response;
    }

    @InterceptCheck(checkers = {AdminChecker.class})
    @PostMapping("/delete_challenge")
    public Response<String> deleteChallenge(@RequestBody @Valid DeleteChallengeRequest deleteChallengeRequest, BindingResult bindingResult) {
        Response<String> response = new Response<>();

        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }
        Long challengeId = deleteChallengeRequest.getChallengeId();

        Challenge challenge = challengeService.getChallengeById(challengeId);
        if (challenge != null) {
            challengeService.deleteByChallengeId(challengeId);
            response.success("删除成功");
        } else {
            response.fail("此题目 ID 不存在");
        }
        return response;
    }

    @InterceptCheck(checkers = {MatchStartedChecker.class})
    @GetMapping("/get_solved_challenge")
    public Response<List<Long>> getSolved() {
        Response<List<Long>> response = new Response<>();
        List<Long> solvedChallenge = new ArrayList<>();
        List<Submission> submissions = submissionService.getBySubmitUserId(authService.getCurrUser().getUserId());

        for (Submission submission : submissions) {
            solvedChallenge.add(submission.getChallengeId());
        }

        response.success("", solvedChallenge);
        return response;
    }

    @InterceptCheck(checkers = {MatchStartedChecker.class})
    @PostMapping("/get_solved_user")
    public Response<ChallengeSolvedUsersWithCount> getSolvedUser(@RequestBody @Valid ChallengeSolvedUserRequest challengeSolvedUserRequest, BindingResult bindingResult) {
        Response<ChallengeSolvedUsersWithCount> response = new Response<>();

        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }
        ChallengeSolvedUsersWithCount challengeSolvedUsersWithCount = new ChallengeSolvedUsersWithCount();
        List<ChallengeSolvedUser> challengeSolvedUsers = challengeService.getSolvedUserByChallengeId(challengeSolvedUserRequest.getChallengeId());
        challengeSolvedUsersWithCount.setSolvedUsers(challengeSolvedUsers);

        Challenge challenge = challengeService.getChallengeById(challengeSolvedUserRequest.getChallengeId());
        if (challenge == null || !challenge.getIsOpen()) {
            challengeSolvedUsersWithCount.setSolvedCount(0);
        } else {
            challengeSolvedUsersWithCount.setSolvedCount(challenge.getSolvedCount());
        }
        response.success("", challengeSolvedUsersWithCount);
        return response;
    }

}
