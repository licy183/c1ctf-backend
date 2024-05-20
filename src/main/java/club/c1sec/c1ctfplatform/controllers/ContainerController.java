package club.c1sec.c1ctfplatform.controllers;
import club.c1sec.c1ctfplatform.checkers.AdminChecker;
import club.c1sec.c1ctfplatform.checkers.LoginChecker;
import club.c1sec.c1ctfplatform.checkers.MatchOpenChecker;
import club.c1sec.c1ctfplatform.interceptor.InterceptCheck;
import club.c1sec.c1ctfplatform.vo.Challenge.ChallengeDetailRequest;
import club.c1sec.c1ctfplatform.vo.Challenge.ChallengeEditRequest;
import club.c1sec.c1ctfplatform.vo.Container.ChallengeContainerSetting;
import club.c1sec.c1ctfplatform.vo.Container.ContainerInfo;
import club.c1sec.c1ctfplatform.vo.Container.ContainerRequest;
import club.c1sec.c1ctfplatform.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@InterceptCheck(checkers = {LoginChecker.class})
@RestController
@RequestMapping("/api/container")
public class ContainerController {
//    @Autowired
//    BulletinService bulletinService;

    @PostMapping("/get_container")
    public Response<ContainerInfo> getContainer(@RequestBody ContainerRequest req) {
        Response<ContainerInfo> response = new Response<>();
//        response.success("", new ContainerInfo());
        response.fail("未申请环境");
        return response;
    }

    @InterceptCheck(checkers = {MatchOpenChecker.class})
    @PostMapping("/alloc_container")
    public Response<String> allocContainer(@RequestBody ContainerRequest req) {
        Response<String> response = new Response<>();
        response.success("环境创建成功");
        return response;
    }

    @PostMapping("/renew_container")
    public Response<String> renewContainer(@RequestBody ContainerRequest req) {
        Response<String> response = new Response<>();
        response.success("环境续期成功");
        return response;
    }

    @PostMapping("/free_container")
    public Response<String> freeContainer(@RequestBody ContainerRequest req) {
        Response<String> response = new Response<>();
        response.success("环境回收成功");
        return response;
    }

    // 返回值：已申请容器环境的challengeId列表
    @GetMapping("/list_container")
    public Response<List<Long>> listContainer() {
        Response<List<Long>> response = new Response<>();
        ArrayList<Long> allocatedContainer = new ArrayList<Long>();
        allocatedContainer.add((long) 1);
        response.success("修改/添加成功", allocatedContainer);
        return response;
    }


    @InterceptCheck(checkers = {AdminChecker.class})
    @PostMapping("/get_challenge_container_setting")
    public Response<ChallengeContainerSetting> getChallengeContainerSetting(@RequestBody ChallengeDetailRequest req) {
        Response<ChallengeContainerSetting> response = new Response<>();
        ChallengeContainerSetting setting = new ChallengeContainerSetting();
        setting.setComposeFile("docker-compose.yaml");
        setting.setIsGenerateFlag(true);
        setting.setUrlTemplate("http://%host%:%port%");
        response.success("ok",setting);
        return response;
    }

    @InterceptCheck(checkers = {AdminChecker.class})
    @PostMapping("/edit_challenge_container_setting")
    public Response<String> editChallengeContainerSetting(@RequestBody ChallengeContainerSetting req) {
        Response<String> response = new Response<>();
        response.success("ok");
        return response;
    }
}
