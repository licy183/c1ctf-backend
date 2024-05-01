package club.c1sec.c1ctfplatform.services;

import club.c1sec.c1ctfplatform.po.User;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JWTService {
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Autowired
    private UserService userService;

    /*
     设置一个 key 为 time, value 为 当前时间 + 100, 数据库中报错为当前时间, 验证时需要验证 jwt 中的时间大于数据库时间,
     将数据库时间往后调, 就能将 jwt 无效化, 相当于销毁.
     */
    public String signToken(Long userId) {
        long currTime = System.currentTimeMillis(); // 86400000 = 24 * 60 * 60 * 1000
        Date expireDate = new Date(currTime + 8640000000L);

        Algorithm algorithm = Algorithm.HMAC256(this.secretKey);
        Builder creator = JWT.create().withClaim("uid", userId).withClaim("time", new Date(currTime + 10000));
        creator = creator.withExpiresAt(expireDate).withIssuer("C1CTF");
        return creator.sign(algorithm);
    }

    public User verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(this.secretKey);
        JWTVerifier verifier = JWT.require(algorithm).withIssuer("C1CTF").build();
        DecodedJWT jwt;
        try {
            jwt = verifier.verify(token);
        } catch (JWTVerificationException exception) {
            return null;
        }
        Long uid = jwt.getClaim("uid").asLong();
        Date time = jwt.getClaim("time").asDate();
        Date exprTime = jwt.getExpiresAt();
        Date currTime = new Date();

        User user = userService.getUserByUserId(uid);

        if (user != null && exprTime.after(currTime) && time.after(user.getLastLoginTime())) {
            return user;
        } else {
            return null;
        }
    }
}
