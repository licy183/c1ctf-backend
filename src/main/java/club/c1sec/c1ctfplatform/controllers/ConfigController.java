package club.c1sec.c1ctfplatform.controllers;

import club.c1sec.c1ctfplatform.checkers.AdminChecker;
import club.c1sec.c1ctfplatform.interceptor.InterceptCheck;
import club.c1sec.c1ctfplatform.services.ConfigService;
import club.c1sec.c1ctfplatform.vo.Config.ConfigEditRequest;
import club.c1sec.c1ctfplatform.vo.Config.MatchInfo;
import club.c1sec.c1ctfplatform.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;

@RestController
@RequestMapping("/api/config")
public class ConfigController {
    @Autowired
    ConfigService configService;

    @GetMapping("/get_match_info")
    public Response<MatchInfo> getMatchInfo() {
        Response<MatchInfo> response = new Response<>();

        Date matchOpenTime = Date.from(configService.getMatchOpenTime());
        Date matchEndTime = Date.from(configService.getMatchEndTime());
        boolean registerOpen = configService.getRegisterOpen();

        MatchInfo info = new MatchInfo();
        info.setEndTime(matchEndTime.getTime());
        info.setOpenTime(matchOpenTime.getTime());
        info.setRegisterOpen(registerOpen);
        response.success("", info);
        return response;
    }

    @InterceptCheck(checkers = {AdminChecker.class})
    @PostMapping("/edit_config")
    public Response<String> editConfig(@RequestBody ConfigEditRequest in) {
        if (in.getOpenTime() != null) {
            configService.setMatchOpenTime(in.getOpenTime());
        }
        if (in.getEndTime() != null) {
            configService.setMatchEndTime(in.getEndTime());
        }
        if (in.getRegisterOpen() != null) {
            configService.setRegisterOpen(in.getRegisterOpen());
        }
        if (in.getDynamicScoreBase() != null) {
            configService.setDynamicScoreBase(in.getDynamicScoreBase());
        }
        if (in.getDynamicScoreMin() != null) {
            configService.setDynamicScoreMin(in.getDynamicScoreMin());
        }
        configService.refreshConfig();
        Response<String> response = new Response<>();
        response.success("修改成功");
        return response;
    }

    @InterceptCheck(checkers = {AdminChecker.class})
    @GetMapping("/get_config")
    public Response<ConfigEditRequest> getConfig() {
        Instant matchOpenTime = configService.getMatchOpenTime();
        Instant matchEndTime = configService.getMatchEndTime();
        boolean registerOpen = configService.getRegisterOpen();
        Integer dynamicScoreBase = configService.getDynamicScoreBase();
        Integer dynamicScoreMin = configService.getDynamicScoreMin();

        ConfigEditRequest config = new ConfigEditRequest();
        config.setOpenTime(matchOpenTime);
        config.setEndTime(matchEndTime);
        config.setRegisterOpen(registerOpen);
        config.setDynamicScoreBase(dynamicScoreBase);
        config.setDynamicScoreMin(dynamicScoreMin);

        Response<ConfigEditRequest> response = new Response<>();
        response.success("", config);
        return response;
    }
}
