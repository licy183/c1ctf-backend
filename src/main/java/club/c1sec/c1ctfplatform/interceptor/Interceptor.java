package club.c1sec.c1ctfplatform.interceptor;

import club.c1sec.c1ctfplatform.checkers.BasicChecker;
import club.c1sec.c1ctfplatform.po.User;
import club.c1sec.c1ctfplatform.services.AuthService;
import club.c1sec.c1ctfplatform.services.JWTService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
public class Interceptor implements HandlerInterceptor {
    @Autowired
    private JWTService jwtService;
    @Autowired
    private AuthService authService;
    @Autowired
    private ApplicationContext context;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws RuntimeException, CheckFailException {
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        String token = httpServletRequest.getHeader("Authorization");

        User user = null;
        if (token != null) {
            token = token.replace("Bearer ", "");
            user = jwtService.verifyToken(token);
        }
        authService.setUser(user);

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        if (method.isAnnotationPresent(InterceptCheck.class)) {
            Class<?>[] classes = method.getAnnotation(InterceptCheck.class).checkers();
            _checkCheckers(classes, method, context, httpServletRequest, httpServletResponse);
        }

        Class<?> methodClass = method.getDeclaringClass();
        if (methodClass.isAnnotationPresent(InterceptCheck.class)) {
            InterceptCheck intercept = methodClass.getAnnotation(InterceptCheck.class);
            Class<?>[] classes = intercept.checkers();
            _checkCheckers(classes, method, context, httpServletRequest, httpServletResponse);
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        AuthService.destroy();
    }

    private static void _checkCheckers(Class<?>[] classes, Method method, ApplicationContext context,
                                       HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws CheckFailException {
        for (Class<?> clazz : classes) {
            BasicChecker checker = (BasicChecker) context.getBean(clazz);
            if (!checker.check(httpServletRequest, httpServletResponse)) {
                httpServletRequest.setAttribute("club.c1sec.CheckError", checker.errorMessage());
                throw new CheckFailException("Check failed in " + clazz.getName() + " with " + method.getName());
            }
        }
    }
}
