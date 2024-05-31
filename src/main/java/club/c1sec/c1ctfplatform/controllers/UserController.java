package club.c1sec.c1ctfplatform.controllers;

import club.c1sec.c1ctfplatform.checkers.AdminChecker;
import club.c1sec.c1ctfplatform.checkers.LoginChecker;
import club.c1sec.c1ctfplatform.checkers.RegisterOpenChecker;
import club.c1sec.c1ctfplatform.enums.UserRole;
import club.c1sec.c1ctfplatform.interceptor.InterceptCheck;
import club.c1sec.c1ctfplatform.limiter.LoginRateLimiter;
import club.c1sec.c1ctfplatform.limiter.MailRateLimiter;
import club.c1sec.c1ctfplatform.po.User;
import club.c1sec.c1ctfplatform.services.*;
import club.c1sec.c1ctfplatform.utils.RandomUtil;
import club.c1sec.c1ctfplatform.utils.StringUtil;
import club.c1sec.c1ctfplatform.vo.Response;
import club.c1sec.c1ctfplatform.vo.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    JWTService jwtService;

    @Autowired
    MailService mailService;

    @Autowired
    UserService userService;

    @Autowired
    AuthService authService;

    @Autowired
    RankingService rankingService;

    @Autowired
    LoginRateLimiter loginRateLimiter;

    @Autowired
    MailRateLimiter mailRateLimiter;

    @Autowired
    CasValidateService casValidateService;

    @Autowired
    BulletinService bulletinService;

    @Autowired
    ConfigService configService;

    @PostMapping("/cas_login")
    public Response<CasLoginResult> casLogin(@RequestBody @Valid CasLoginRequest casLoginRequest,
                                             BindingResult bindingResult, HttpServletRequest httpServletRequest) {
        Response<CasLoginResult> response = new Response<>();
        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }
        // F**k the wrdvpn!
        // 网瑞达的 WebVPN 并不会记录 X-Real-IP，也不会记录 X-Forward-For
        if (configService.getLoginLimit()) {
            String ip = httpServletRequest.getHeader("x-real-ip");
            if (ip != null) {
                if (!loginRateLimiter.check(ip)) {
                    response.invalid("登录过于频繁, 请稍等一会再试试");
                    return response;
                }
            }
        }

        String ticket = casLoginRequest.getTicket();
        Map.Entry<String, String> res = casValidateService.casTicketValidate(ticket);
        if (res == null) {
            response.fail("CAS 登录失败");
            return response;
        }
        String stuId = res.getKey();
        String name = res.getValue();
        CasLoginResult result = new CasLoginResult();
        result.setStudentId(stuId);
        result.setStudentName(name);
        // 在数据库中查找，判断是否需要注册
        if (userService.isStudentIdExist(stuId)) {
            // 设置登录时间
            User user = userService.getUserByStuId(stuId);
            user.setLastLoginTime(new Date());
            userService.addUser(user);
            // 不需要注册
            result.setNeedRegister(false);
            result.setToken(jwtService.signToken(userService.getUserByStuId(stuId).getUserId()));
        } else {
            // 需要注册
            if (!configService.getRegisterOpen()) {
                response.fail("平台注册已经关闭");
                return response;
            }
            result.setNeedRegister(true);
            result.setUuid(casValidateService.casPreRegister(stuId, name));
        }
        response.success("", result);
        return response;
    }

    @InterceptCheck(checkers = {RegisterOpenChecker.class})
    @PostMapping("/cas_register")
    public Response<?> casRegister(@RequestBody @Valid CasRegisterRequest casRegisterRequest,
                                   BindingResult bindingResult) {
        Response<Object> response = new Response<>();
        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }
        final String username = casRegisterRequest.getUsername();
        final String email = casRegisterRequest.getEmail();
        final String uuid = casRegisterRequest.getUuid();
        if (userService.isUsernameExist(username)) {
            response.fail("用户名已被注册");
            return response;
        }
        if (userService.isEmailExist(email)) {
            response.fail("该邮箱已被注册");
            return response;
        }
        Map.Entry<String, String> idNamePair = casValidateService.getCasPreRegisterInfo(uuid);
        if (idNamePair == null) {
            response.fail("身份认证过期，请从比赛平台重新登录。");
            return response;
        }
        final String stuId = idNamePair.getKey();
        final String name = idNamePair.getValue();
        if (userService.isStudentIdExist(stuId)) {
            response.fail("你已经注册过了，请尝试使用 CAS 重新登录。");
            return response;
        }
        String password = casRegisterRequest.getPassword();
        if (password.length() < 5) {
            response.fail("密码太短, 请换一个更强的密码");
            return response;
        }
        User user = new User();
        password = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setUsername(username);
        user.setPassword(password);
        user.setUserRole(UserRole.USER_ROLE_STUDENT);
        user.setStudentId(stuId);
        user.setStudentName(name);
        user.setBanned(false);
        user.setEmail(email);
        user.setUserSeed(RandomUtil.getRandomPrime());
        user.setLastLoginTime(new Date());
        userService.addUser(user);
        response.success("注册成功");
        return response;
    }

    @PostMapping("/login")
    public Response<LoginInfo> login(@RequestBody @Valid LoginRequest loginRequest,
                                     BindingResult bindingResult, HttpServletRequest httpServletRequest) {
        Response<LoginInfo> response = new Response<>();
        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }

        if (configService.getLoginLimit()) {
            String ip = httpServletRequest.getHeader("x-real-ip");
            if (ip != null) {
                if (!loginRateLimiter.check(ip)) {
                    response.invalid("登录过于频繁, 请稍等一会再试试");
                    return response;
                }
            }
        }

        User user = userService.getUserByUsername(loginRequest.getUsername());
        if (user != null && BCrypt.checkpw(loginRequest.getPassword(), user.getPassword())) {
            user.setLastLoginTime(new Date());
            userService.addUser(user);
            String token = jwtService.signToken(user.getUserId());
            response.success("登录成功",
                    new LoginInfo(user.getUsername(), token, user.getUserRole() == UserRole.USER_ROLE_ADMIN));
        } else {
            response.invalid("用户名或者密码输入错误");
        }
        return response;
    }

    @InterceptCheck(checkers = {RegisterOpenChecker.class})
    @PostMapping("/register")
    public Response<String> register(@RequestBody @Valid RegisterRequest registerRequest,
                                     BindingResult bindingResult) {
        Response<String> response = new Response<>();
        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }
        boolean isStudent = registerRequest.getIsStudent();

        // 使用 CAS 登录时，不应该能够通过本接口注册成为学生
        if (isStudent) {
            response.fail("本校学生请使用CAS登录");
            return response;
        }

        String username = registerRequest.getUsername();
        String password = registerRequest.getPassword();
        String email = registerRequest.getEmail();
        String verifyCode = registerRequest.getVerifyCode();
        UserRole userRole;
        User user = new User();

        if (!mailService.verifyRegisterMail(email, verifyCode)) {
            response.fail("mail_code_error");
            return response;
        }
        if (username.length() > 32 || username.length() < 4 || !StringUtil.isEmail(email)) {
            response.invalid("无效参数");
            return response;
        }
        if (password.length() < 5) {
            response.fail("密码太短, 请换一个更强的密码");
            return response;
        }
        if (userService.isUsernameExist(username)) {
            response.fail("username_error");
            return response;
        }
        if (userService.isEmailExist(email)) {
            response.fail("email_error");
            return response;
        }
        userRole = UserRole.USER_ROLE_PLAIN;
        user.setStudentName(null);
        user.setStudentId(null);

        mailService.discardEmailVerifyCode(email); // 删除 Redis 中的验证码

        password = BCrypt.hashpw(password, BCrypt.gensalt());
        user.setUsername(username);
        user.setPassword(password);
        user.setUserRole(userRole);
        user.setBanned(false);
        user.setEmail(email);
        user.setUserSeed(RandomUtil.getRandomPrime());
        user.setLastLoginTime(new Date());
        userService.addUser(user);

        response.success("注册成功");
        return response;
    }

    @PostMapping("/check_username")
    public Response<String> checkUsername(@RequestBody @Valid CheckUsernameRequest checkUsernameRequest,
                                          BindingResult bindingResult) {
        Response<String> response = new Response<>();
        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }

        if (userService.isUsernameExist(checkUsernameRequest.getUsername())) {
            response.fail("用户名已被使用");
        } else {
            response.success(null);
        }
        return response;
    }

    @PostMapping("/send_verify_mail")
    public Response<String> sendRegisterMail(@RequestBody @Valid SendRegisterMailRequest sendRegisterMailRequest,
                                             BindingResult bindingResult) {
        Response<String> response = new Response<>();
        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }

        String email = sendRegisterMailRequest.getEmail();
        Integer mailType = sendRegisterMailRequest.getType();

        if (StringUtil.isEmail(email)) {
            if (mailRateLimiter.check(email)) {
                mailService.sendRegisterMail(email, mailType);
                response.success("发送成功", "验证码已发送");
            } else {
                response.fail("操作太过频繁, 稍等一下再试试吧");
            }
        } else {
            response.fail("无效的邮件地址");
        }
        return response;
    }

    @PostMapping("/forget_password")
    public Response<String> forgetPassword(@RequestBody @Valid ForgetPasswordRequest forgetPasswordRequest,
                                           BindingResult bindingResult) {
        Response<String> response = new Response<>();

        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }

        String email = forgetPasswordRequest.getEmail();
        String verifyCode = forgetPasswordRequest.getVerifyCode();
        String newPassword = forgetPasswordRequest.getNewPassword();
        String username = forgetPasswordRequest.getUsername();

        if (newPassword.length() < 5) {
            response.fail("密码太短, 请换一个更强的密码");
        } else {
            if (mailService.verifyRegisterMail(email, verifyCode)) {
                User user = userService.getUserByEmail(email);
                if (user != null && user.getUsername().equals(username)) {
                    mailService.discardEmailVerifyCode(email); // 删除 Redis 中的验证码
                    user.setLastLoginTime(new Date());
                    user.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
                    userService.addUser(user);
                    response.success("重置成功");
                } else {
                    response.fail("用户名或邮箱错误");
                }
            } else {
                response.fail("邮件验证码不正确");
            }
        }
        return response;
    }

    @InterceptCheck(checkers = LoginChecker.class)
    @PostMapping("/change_password")
    public Response<String> changePassword(@RequestBody @Valid ChangePasswordRequest changePasswordRequest,
                                           BindingResult bindingResult) {
        Response<String> response = new Response<>();

        if (bindingResult.hasErrors()) {
            response.invalid(bindingResult.getFieldError().getDefaultMessage());
            return response;
        }

        String oldPassword = changePasswordRequest.getOldPassword();
        String newPassword = changePasswordRequest.getNewPassword();

        if (newPassword.length() < 5) {
            response.fail("密码太短, 请换一个更强的密码");
        } else {
            User currUser = authService.getCurrUser();
            if (BCrypt.checkpw(oldPassword, currUser.getPassword())) {
                currUser.setPassword(BCrypt.hashpw(newPassword, BCrypt.gensalt()));
                currUser.setLastLoginTime(new Date());
                userService.addUser(currUser);
                response.success("密码更改成功");
            } else {
                response.fail("旧密码错误, 请确认输入正确");
            }
        }
        return response;
    }

    @InterceptCheck(checkers = LoginChecker.class)
    @GetMapping("/get_curr_user")
    public Response<UserInfo> getCurrUser() {
        User currUser = authService.getCurrUser();
        UserInfo userInfo = new UserInfo();
        Response<UserInfo> response = new Response<>();
        Integer rank = rankingService.getReverseRanking().get(currUser.getUserId());
        if (rank == null) {
            rank = -1;
        }

        userInfo.setBanned(currUser.getBanned());
        userInfo.setEmail(currUser.getEmail());
        userInfo.setIsAdmin(currUser.getUserRole() == UserRole.USER_ROLE_ADMIN);
        userInfo.setIsStudent(currUser.getUserRole() == UserRole.USER_ROLE_STUDENT);
        userInfo.setStudentId(currUser.getStudentId());
        userInfo.setStudentName(currUser.getStudentName());
        userInfo.setUsername(currUser.getUsername());
        userInfo.setRank(rank);
        userInfo.setHaveUnreadBulletin(bulletinService.haveUnreadBulletin(currUser.getUserId()));

        response.success("", userInfo);
        return response;
    }

    @InterceptCheck(checkers = {AdminChecker.class})
    @PostMapping("/edit_user")
    public Response<Long> editUser(@RequestBody EditUserRequest editUserRequest) {
        Response<Long> response = new Response<>();
        User user = new User();

        Long userId = editUserRequest.getUserId();
        UserRole userRole = editUserRequest.getUserRole();
        String username = editUserRequest.getUsername();
        String password = editUserRequest.getPassword();
        String email = editUserRequest.getEmail();
        String studentId = editUserRequest.getStudentId();
        String studentName = editUserRequest.getStudentName();
        Boolean banned = editUserRequest.getBanned();

        if (password != null) {
            password = BCrypt.hashpw(password, BCrypt.gensalt());
        }

        if (userId != null) {
            user = userService.getUserByUserId(userId);
            if (user != null) {
                if (userRole != null) {
                    user.setUserRole(userRole);
                }
                if (username != null) {
                    user.setUsername(username);
                }
                if (password != null) {
                    user.setPassword(password);
                }
                if (email != null) {
                    user.setEmail(email);
                }
                if (studentId != null) {
                    user.setStudentId(studentId);
                }
                if (studentName != null) {
                    user.setStudentName(studentName);
                }
                if (banned != null) {
                    user.setBanned(banned);
                }
                userService.addUser(user);
                response.success("修改成功", userId);
            } else {
                response.fail("用户不存在", -1L);
            }
        } else {
            user.setUserRole(userRole);
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setStudentId(studentId);
            user.setStudentName(studentName);
            user.setBanned(banned);
            user.setUserSeed(RandomUtil.getRandomPrime());
            userService.addUser(user);
            response.success("创建成功", user.getUserId());
        }
        return response;
    }

    @InterceptCheck(checkers = {AdminChecker.class})
    @GetMapping("/get_all_user")
    public Response<List<User>> getAllUser() {
        Response<List<User>> response = new Response<>();
        response.success("", userService.getAllUser());
        return response;
    }
}
