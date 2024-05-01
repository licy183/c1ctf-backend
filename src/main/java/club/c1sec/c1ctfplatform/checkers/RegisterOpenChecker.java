package club.c1sec.c1ctfplatform.checkers;

import club.c1sec.c1ctfplatform.services.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class RegisterOpenChecker implements BasicChecker {
    @Autowired
    private ConfigService config;

    @Override
    public boolean check(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return config.getRegisterOpen();
    }

    @Override
    public String errorMessage() {
        return "Register is close currently";
    }
}
