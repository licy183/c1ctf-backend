package club.c1sec.c1ctfplatform.services;

import club.c1sec.c1ctfplatform.utils.RandomUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class MailService {
    private final String mailCodeRedisKey = "MAIL_CODE_";
    private final String charset = "0123456789ABCEFGHJKLMNPRSTUVWXYZ";

    public final static int MAIL_TYPE_REGISTER = 0;
    public final static int MAIL_TYPE_FORGET_PASSWORD = 1;

    @Autowired
    private RedisService redisService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private AsyncExecutorService asyncExecutorService;

    @Value("${spring.mail.username}")
    private String sender;

    private void sendMail(String sender, String to, String title, String msg) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(sender);
            helper.setTo(to);
            helper.setSubject(title);
            helper.setText(msg, false);
            message.setHeader("Message-ID", "C1Sec");
            mailSender.send(message);
        } catch (MessagingException ignored) {
        }
    }

    public void sendRegisterMail(String mailAddress, int mailType) {
        String redisKey = this.mailCodeRedisKey + mailAddress;
        String code = RandomUtil.getRandomString(6, this.charset);

        redisService.setKeyValueWithExpire(redisKey, code, 300); // 邮件 5 分钟时间有效
        asyncExecutorService.execute(() -> {
            sendMail(sender, mailAddress, getMailTitleTemplate(mailType), getMailBodyTemplate(mailType, code));
        });
    }

    public Boolean verifyRegisterMail(String mailAddress, String code) {
        String redisKey = this.mailCodeRedisKey + mailAddress;
        if (redisService.isKeyExist(redisKey)) {
            return redisService.getValue(redisKey).equals(code.toUpperCase().strip());
        } else {
            return false;
        }
    }

    public void discardEmailVerifyCode(String mailAddress) {
        String redisKey = this.mailCodeRedisKey + mailAddress;
        redisService.deleteKey(redisKey);
    }

    public String getMailTitleTemplate(int type) {
        switch (type) {
            case MAIL_TYPE_FORGET_PASSWORD:
                return "[C1CTF] 找回密码";
            case MAIL_TYPE_REGISTER:
                return "[C1CTF] 确认注册";
            default:
                return "";
        }
    }

    public String getMailBodyTemplate(int type, String code) {
        switch (type) {
            case MAIL_TYPE_FORGET_PASSWORD:
                return "您的找回验证码是 [" + code + "] 请于 5 分钟内填写, 如非本人操作, 请忽略本邮件.";
            case MAIL_TYPE_REGISTER:
                return "您的注册验证码是 [" + code + "] 请于 5 分钟内填写, 如非本人操作, 请忽略本邮件.";
            default:
                return "";
        }
    }
}
