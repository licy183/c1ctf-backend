package club.c1sec.c1ctfplatform.controllers;

import club.c1sec.c1ctfplatform.checkers.*;
import club.c1sec.c1ctfplatform.enums.ContainerStatus;
import club.c1sec.c1ctfplatform.interceptor.InterceptCheck;
import club.c1sec.c1ctfplatform.po.*;
import club.c1sec.c1ctfplatform.services.*;
import club.c1sec.c1ctfplatform.utils.FlagUtil;
import club.c1sec.c1ctfplatform.vo.challenge.ChallengeDetailRequest;
import club.c1sec.c1ctfplatform.vo.container.*;
import club.c1sec.c1ctfplatform.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
@InterceptCheck(checkers = {LoginChecker.class})
@RestController
@RequestMapping("/api/container")
public class ContainerController {

    @Value("${container.create_url}")
    public String createUrl;
    @Value("${container.get_url}")
    public String getUrl;
    @Value("${container.renew_url}")
    public String renewUrl;
    @Value("${container.delete_url}")
    public String deleteUrl;
    @Value("${container.list_url}")
    public String listUrl;

    @Autowired
    AuthService authService;
    @Autowired
    ChallengeService challengeService;
    @Autowired
    ConfigService configService;
    @Autowired
    ContainerService containerService;
    @Autowired
    HttpService httpService;

    @InterceptCheck(checkers = {LoginChecker.class})
    @PostMapping("/get_container")
    public Response<ContainerInfo> getContainer(@RequestBody @Valid ContainerRequest req, BindingResult bindingResult) {
        Response<ContainerInfo> response = new Response<>();
        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }
        Long challengeId = req.getChallengeId();
        User currUser = authService.getCurrUser();
        ContainerInfo info = new ContainerInfo();
        /* 先查数据库，是否存在该用户和题目ID的容器。*/
        Container container = containerService.getContainerIdByPK(currUser.getUserId(), challengeId);
        /* 如果数据库中没有，那就是没有申请。 */
        if (container == null) {
            info.setIsAllocated(false);
            info.setIsCreated(false);
            response.fail("未申请环境", info);
            return response;
        }
        Long containerId = container.getEnvId();
        ContaineredChallenge containeredChallenge = challengeService.getContaineredChallengeConfigByChallengeId(challengeId);
        if (container.getExpire() == null) {
            info.setExpireTime(Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli());
        } else {
            info.setExpireTime(container.getExpire().toEpochMilli());
        }
        info.setIsAllocated(!container.getStatus().equals(club.c1sec.c1ctfplatform.enums.ContainerStatus.DELETED));
        info.setIsCreated(container.getStatus().equals(club.c1sec.c1ctfplatform.enums.ContainerStatus.CREATED));
        String url = containeredChallenge.getUrlTemplate();
        if (container.getHost() != null && container.getPort() != null) {
            info.setUrl(url.replace("%host%", container.getHost()).replace("%port%", container.getPort().toString()));
        } else {
            info.setUrl(url);
        }
        /* 向管理端更新状态 */
        try {
            GetContainerResp resp =
                    httpService.sendGetRequest(getUrl + containerId, Collections.emptyMap(), GetContainerResp.class);
            ContainerStatus status = resp.getStatus();
            if (status.equals(ContainerStatus.PROCESS_ERROR)) {
                response.fail("获取容器状态失败，请联系管理员。");
                return response;
            }
            container.setStatus(status);
            container.setHost(resp.getHost());
            container.setPort(Integer.valueOf(resp.getPort()));
            container.setExpire(resp.getExpire());
            containerService.saveContainer(container);
            info.setExpireTime(container.getExpire().toEpochMilli());
            info.setIsAllocated(!container.getStatus().equals(club.c1sec.c1ctfplatform.enums.ContainerStatus.DELETED));
            info.setIsCreated(container.getStatus().equals(club.c1sec.c1ctfplatform.enums.ContainerStatus.CREATED));
            if (info.getIsCreated()) {
                info.setUrl(url.replace("%host%", container.getHost()).replace("%port%", container.getPort().toString()));
            }
            response.success("", info);
        } catch (Exception e) {
            log.error("getContainer()", e);
            // 更新容器状态失败
            response.fail("获取容器状态失败，请联系管理员。");
        }
        return response;
    }

    @InterceptCheck(checkers = {MatchOpenChecker.class, LoginChecker.class})
    @PostMapping("/alloc_container")
    public Response<String> allocContainer(@RequestBody @Valid ContainerRequest req, BindingResult bindingResult) {
        Response<String> response = new Response<>();
        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }
        Long challengeId = req.getChallengeId();
        User currUser = authService.getCurrUser();
        Long currUserId = currUser.getUserId();
        /* 保证Challenge是使用容器的 */
        Challenge challenge = challengeService.getChallengeById(challengeId);
        if (!challenge.getIsContainer()) {
            response.invalid("该题目不是使用容器的题目");
            return response;
        }
        /* 用户不应该申请很多的容器 */
        Long count = containerService.countContainerByUserId(currUserId);
        int maxCount = configService.getContainerCount();
        if (count > maxCount) {
            response.fail("您已经申请了很多容器了，请先关闭几个再申请吧");
            return response;
        }
        /* 先查数据库，是否存在该用户和题目ID的容器。*/
        Container container = containerService.getContainerIdByPK(currUserId, challengeId);
        /* 如果数据库中没有，那就是没有申请。 */
        if (container != null && container.getStatus() != club.c1sec.c1ctfplatform.enums.ContainerStatus.DELETED) {
            response.fail("您已申请过该题目的容器");
            return response;
        }
        Map<String, Object> param = new HashMap<>();
        /* 生成容器配置 */
        ContaineredChallenge containeredChallenge =
                challengeService.getContaineredChallengeConfigByChallengeId(challengeId);
        param.put("uid", currUserId);
        param.put("challenge", challengeId);
        param.put("compose_file", containeredChallenge.getComposeFile());
        String flag;
        if (!containeredChallenge.getIsGenerateFlag()) {
            param.put("flag", flag = containeredChallenge.getFlag());
        } else {
            String flagTemplate = configService.getContainerFlagFormat();
            param.put("flag", flag = FlagUtil.generatorFlag(flagTemplate));
        }
        /* 向管理端申请容器 */
        try {
            CreateContainerResp resp = httpService.sendPostRequest(createUrl, param, CreateContainerResp.class);
            ContainerStatus status = resp.getStatus();
            if (status.equals(ContainerStatus.PROCESS_ERROR)) {
                response.fail("申请容器失败");
                return response;
            }
            Long envId = resp.getId();
            /* 存入数据库 */
            container = new Container();
            container.setUserId(currUserId);
            container.setChallengeId(challengeId);
            container.setEnvId(envId);
            container.setStatus(club.c1sec.c1ctfplatform.enums.ContainerStatus.CREATING);
            container.setFlag(flag);
            containerService.saveContainer(container);
            response.success("申请容器成功");
        } catch (Exception e) {
            // 申请容器失败
            response.fail("申请容器失败");
            log.error("allocContainer()", e);
        }
        return response;
    }

    @InterceptCheck(checkers = {MatchOpenChecker.class, LoginChecker.class})
    @PostMapping("/renew_container")
    public Response<String> renewContainer(@RequestBody @Valid ContainerRequest req, BindingResult bindingResult) {
        Response<String> response = new Response<>();
        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }
        Long challengeId = req.getChallengeId();
        User currUser = authService.getCurrUser();
        Long currUserId = currUser.getUserId();
        /* 保证 Challenge 是使用容器的 */
        Challenge challenge = challengeService.getChallengeById(challengeId);
        if (!challenge.getIsContainer()) {
            response.invalid("该题目不是使用容器的题目");
            return response;
        }
        /* 先查数据库，是否存在该用户和题目ID的容器。*/
        Container container = containerService.getContainerIdByPK(currUserId, challengeId);
        /* 如果数据库中没有，那就是没有申请。 */
        if (container == null) {
            response.fail("您未申请过该题目的容器");
            return response;
        }
        /* 向管理端申请续期 */
        Long envId = container.getEnvId();
        Map<String, Object> param = new HashMap<>();
        param.put("expire", DateTimeFormatter.ISO_OFFSET_DATE_TIME.toFormat()
                .format(Instant.now().plus(1, ChronoUnit.HOURS)));
        try {
            RenewContainerResp resp =
                    httpService.sendPostRequest(renewUrl + envId, param, RenewContainerResp.class);
            ContainerStatus status = resp.getStatus();
            if (status.equals(ContainerStatus.PROCESS_ERROR)) {
                response.fail("续期环境失败");
                return response;
            }
            Instant expire = resp.getExpire();
            if (expire == null) {
                response.fail("续期环境失败");
                return response;
            }
            /* 更新数据库 */
            container.setExpire(expire);
            containerService.saveContainer(container);
            response.success("续期容器成功");
        } catch (Exception e) {
            // 续期容器失败
            response.fail("续期容器失败");
            log.error("renewContainer()", e);
        }
        return response;
    }

    @InterceptCheck(checkers = {MatchOpenChecker.class, LoginChecker.class})
    @PostMapping("/free_container")
    public Response<String> freeContainer(@RequestBody @Valid ContainerRequest req, BindingResult bindingResult) {
        Response<String> response = new Response<>();
        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }
        Long challengeId = req.getChallengeId();
        User currUser = authService.getCurrUser();
        Long currUserId = currUser.getUserId();
        /* 保证Challenge是使用容器的 */
        Challenge challenge = challengeService.getChallengeById(challengeId);
        if (!challenge.getIsContainer()) {
            response.invalid("该题目不是使用容器的题目");
            return response;
        }
        /* 先查数据库，是否存在该用户和题目ID的容器。*/
        Container container = containerService.getContainerIdByPK(currUserId, challengeId);
        /* 如果数据库中没有，那就是没有申请。 */
        if (container == null) {
            response.fail("您未申请过该题目的容器");
            return response;
        }
        Long envId = container.getEnvId();
        /* 向管理端申请回收 */
        try {
            DeleteContainerResp resp =
                    httpService.sendGetRequest(deleteUrl + envId, Collections.emptyMap(), DeleteContainerResp.class);
            ContainerStatus status = resp.getStatus();
            if (status.equals(ContainerStatus.DELETED)) {
                /* 更新数据库 */
                container.setStatus(club.c1sec.c1ctfplatform.enums.ContainerStatus.DELETED);
                containerService.saveContainer(container);
                response.success("回收容器成功");
                return response;
            }
            response.fail("回收容器失败");
        } catch (Exception e) {
            // 回收容器失败
            response.fail("回收容器失败");
            log.error("freeContainer()", e);
        }
        return response;
    }

    // 返回值：已申请容器环境的challengeId列表
    @InterceptCheck(checkers = {LoginChecker.class})
    @GetMapping("/list_container")
    public Response<List<Long>> listContainer() {
        Response<List<Long>> response = new Response<>();
        User currUser = authService.getCurrUser();
        Long currUserId = currUser.getUserId();
        List<Long> allocatedContainer = containerService.getAllChallengeIdNotDeleted(currUserId);
        response.success("", allocatedContainer);
        return response;
    }


    @InterceptCheck(checkers = {AdminChecker.class})
    @PostMapping("/get_challenge_container_setting")
    public Response<ChallengeContainerSetting> getChallengeContainerSetting(@RequestBody @Valid ChallengeDetailRequest req, BindingResult bindingResult) {
        Response<ChallengeContainerSetting> response = new Response<>();
        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }
        Long challengeId = req.getChallengeId();
        ContaineredChallenge containeredChallenge = challengeService.getContaineredChallengeConfigByChallengeId(challengeId);
        ChallengeContainerSetting setting = new ChallengeContainerSetting();
        if (containeredChallenge == null) {
            setting.setChallengeId(challengeId);
            setting.setComposeFile("test/docker-compose.yaml");
            setting.setIsGenerateFlag(false);
            setting.setFlag("c1ctf{This_is_an_example_flag}");
            setting.setUrlTemplate("http://%host%:%port%");
        } else {
            setting.setComposeFile(containeredChallenge.getComposeFile());
            setting.setIsGenerateFlag(containeredChallenge.getIsGenerateFlag());
            setting.setChallengeId(containeredChallenge.getChallengeId());
            setting.setUrlTemplate(containeredChallenge.getUrlTemplate());
            setting.setFlag(containeredChallenge.getFlag());
        }
        response.success("", setting);
        return response;
    }

    @InterceptCheck(checkers = {AdminChecker.class})
    @PostMapping("/edit_challenge_container_setting")
    public Response<String> editChallengeContainerSetting(@RequestBody @Valid ChallengeContainerSetting req, BindingResult bindingResult) {
        Response<String> response = new Response<>();
        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }
        ContaineredChallenge entity = new ContaineredChallenge();
        entity.setChallengeId(req.getChallengeId());
        entity.setComposeFile(req.getComposeFile());
        entity.setIsGenerateFlag(req.getIsGenerateFlag());
        entity.setUrlTemplate(req.getUrlTemplate());
        entity.setFlag(req.getFlag());
        challengeService.saveContaineredChallenge(entity);
        response.success("保存成功");
        return response;
    }
}
