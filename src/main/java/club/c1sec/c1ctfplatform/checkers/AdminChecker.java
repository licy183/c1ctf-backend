package club.c1sec.c1ctfplatform.checkers;

import club.c1sec.c1ctfplatform.enums.UserRole;
import club.c1sec.c1ctfplatform.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Service
public class AdminChecker implements BasicChecker {
    @Autowired
    AuthService authService;

    @Override
    public boolean check(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return !authService.isAnonymous() && authService.getCurrUser().getUserRole() == UserRole.USER_ROLE_ADMIN;
    }

    @Override
    public String errorMessage() {
        return "Permission error";
    }
}
