package club.c1sec.c1ctfplatform.checkers;

import club.c1sec.c1ctfplatform.services.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

@Service
public class MatchOpenChecker implements BasicChecker {
    @Autowired
    ConfigService configService;

    @Override
    public boolean check(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return configService.getMatchOpen();
    }

    @Override
    public String errorMessage() {
        return "The match is close currently";
    }
}
