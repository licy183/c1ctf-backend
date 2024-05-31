package club.c1sec.c1ctfplatform.checkers;

import club.c1sec.c1ctfplatform.po.User;
import club.c1sec.c1ctfplatform.services.AuthService;
import club.c1sec.c1ctfplatform.services.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Service
public class ContainerConcurrentChecker implements BasicChecker {
    private static final String CONCURRENT_CHECKER_KEY_PREFIX = "CONTAINER_CONCURRENT_CHECKER_";

    @Autowired
    private AuthService authService;

    @Autowired
    private RedisService redisService;

    @Override
    public boolean check(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        User user = authService.getCurrUser();
        // 当用户已登陆时才进行检测
        if (user == null) return true;
        String key = CONCURRENT_CHECKER_KEY_PREFIX + user.getUserId();
        if (redisService.isKeyExist(key)) return false;
        // 5s 内仅允许一次操作
        redisService.setKeyValueWithExpire(key, "1", 5);
        return true;
    }

    @Override
    public String errorMessage() {
        return "Requests too fast";
    }
}
