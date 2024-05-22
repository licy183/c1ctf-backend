package club.c1sec.c1ctfplatform.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.math3.primes.Primes;

import java.security.SecureRandom;

@UtilityClass
public class RandomUtil {
    public static String getRandomString(Integer length, String charset) {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++) {
            sb.append(charset.charAt(random.nextInt(charset.length())));
        }
        return sb.toString();
    }

    public static int getRandomPrime() {
        return Primes.nextPrime(new SecureRandom().nextInt(999999));
    }
}
