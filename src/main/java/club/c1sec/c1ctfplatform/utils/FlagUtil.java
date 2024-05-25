package club.c1sec.c1ctfplatform.utils;

import lombok.experimental.UtilityClass;

import java.util.UUID;

@UtilityClass
public class FlagUtil {
    public static String generateFlag(String flagTemplate) {
        String prefix = flagTemplate.substring(0, flagTemplate.length() - 2);
        return prefix + "{" + UUID.randomUUID() + "}";
    }
}
