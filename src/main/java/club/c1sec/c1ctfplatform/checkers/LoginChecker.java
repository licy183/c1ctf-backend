package club.c1sec.c1ctfplatform.checkers;

import club.c1sec.c1ctfplatform.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class LoginChecker implements BasicChecker {
    @Autowired
    AuthService authService;

    @Override
    public boolean check(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return !authService.isAnonymous();
    }

    @Override
    public String errorMessage() {
        return "Please login first";
    }
}
