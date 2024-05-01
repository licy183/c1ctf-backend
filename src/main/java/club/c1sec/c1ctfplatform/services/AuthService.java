package club.c1sec.c1ctfplatform.services;

import club.c1sec.c1ctfplatform.po.User;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private static final ThreadLocal<User> user = new ThreadLocal<>();

    public void setUser(User user) {
        AuthService.user.set(user);
    }

    public Boolean isAnonymous() {
        return this.getCurrUser() == null;
    }

    public User getCurrUser() {
        return AuthService.user.get();
    }

    public static void destroy() {
        AuthService.user.remove();
    }
}
