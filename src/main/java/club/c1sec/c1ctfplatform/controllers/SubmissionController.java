package club.c1sec.c1ctfplatform.controllers;

import club.c1sec.c1ctfplatform.checkers.AdminChecker;
import club.c1sec.c1ctfplatform.checkers.LoginChecker;
import club.c1sec.c1ctfplatform.checkers.MatchOpenChecker;
import club.c1sec.c1ctfplatform.enums.LogEvent;
import club.c1sec.c1ctfplatform.interceptor.InterceptCheck;
import club.c1sec.c1ctfplatform.limiter.SubmitRateLimiter;
import club.c1sec.c1ctfplatform.po.Submission;
import club.c1sec.c1ctfplatform.po.User;
import club.c1sec.c1ctfplatform.services.*;
import club.c1sec.c1ctfplatform.vo.Attachment.AttachmentChallengeInfo;
import club.c1sec.c1ctfplatform.vo.Response;
import club.c1sec.c1ctfplatform.vo.Submission.GetUserSubmitRequest;
import club.c1sec.c1ctfplatform.vo.Submission.SubmitFlagRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@InterceptCheck(checkers = LoginChecker.class)
@RestController
@RequestMapping("/api/submission")
public class SubmissionController {
    @Autowired
    SubmissionService submissionService;

    @Autowired
    SubmitRateLimiter submitRateLimiter;

    @Autowired
    ChallengeService challengeService;

    @Autowired
    AuthService authService;

    @Autowired
    LogService logService;

    @Autowired
    RankingService rankingService;

    @InterceptCheck(checkers = {MatchOpenChecker.class})
    @PostMapping("/submit_flag")
    public Response<Long> submitFlag(@RequestBody @Valid SubmitFlagRequest submitFlagRequest, BindingResult bindingResult) {
        Response<Long> response = new Response<>();

        User currentUser = authService.getCurrUser();
        if (currentUser.getBanned()) {
            response.fail("您已被封号, 无法再提交 flag");
            return response;
        }

        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }

        if (!submitRateLimiter.check(currentUser.getUsername())) {
            response.fail("提交过于频繁, 一分钟后再试试吧");
            return response;
        }

        String flag = submitFlagRequest.getFlag();

        AttachmentChallengeInfo challenge = challengeService.getChallengeByFlag(flag);

        if (challenge != null && challenge.isChallengeIsOpen()) {
            if (!submissionService.isSubmissionExist(challenge.getChallengeId(), currentUser.getUserId())) {
                Submission submission = new Submission();
                submission.setChallengeId(challenge.getChallengeId());
                submission.setSubmitTime(new Date());
                submission.setSubmitUserId(currentUser.getUserId());
                submissionService.addSubmission(submission);
                response.success("提交成功", challenge.getChallengeId());

                rankingService.refreshOneDynamicScore(challenge.getChallengeId()); // 刷新这个题目的动态分数 & 解题人数

                if (submissionService.isSubmissionValid(challenge.getChallengeId(), challenge.getAttachmentId(), challenge.getChallengeSeed(), currentUser.getUserSeed())) {
                    logService.log(LogEvent.LOG_EVENT_SUBMIT_SUCCESS, currentUser.getUsername(), flag, challenge.getChallengeId().toString());
                } else {
                    logService.log(LogEvent.LOG_EVENT_SUBMIT_OTHER_USER, currentUser.getUsername(), flag, challenge.getChallengeId().toString());
                }
            } else {
                response.fail("你已经提交过该flag了");

                if (submissionService.isSubmissionValid(challenge.getChallengeId(), challenge.getAttachmentId(), challenge.getChallengeSeed(), currentUser.getUserSeed())) {
                    logService.log(LogEvent.LOG_EVENT_SUBMIT_REPEAT, currentUser.getUsername(), flag, challenge.getChallengeId().toString());
                } else {
                    logService.log(LogEvent.LOG_EVENT_SUBMIT_REPEAT_OTHER_USER, currentUser.getUsername(), flag, challenge.getChallengeId().toString());
                }
            }
        } else {
            response.fail("无效flag");
            logService.log(LogEvent.LOG_EVENT_SUBMIT_ERROR, currentUser.getUsername(), flag);
        }
        return response;
    }

    @InterceptCheck(checkers = {AdminChecker.class})
    @PostMapping("/get_user_submits")
    public Response<List<Submission>> getUserSubmit(@RequestBody @Valid GetUserSubmitRequest getUserSubmitRequest, BindingResult bindingResult) {
        Response<List<Submission>> response = new Response<>();
        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }

        Long userId = getUserSubmitRequest.getUserId();
        List<Submission> submissions = submissionService.getBySubmitUserId(userId);
        response.success("", submissions);
        return response;
    }
}