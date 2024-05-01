package club.c1sec.c1ctfplatform.utils;

import java.util.regex.Pattern;

public class StringUtil {
    public static String notNull(String string) {
        if (string == null) {
            return "";
        } else {
            return string;
        }
    }

    public static Boolean isStudentId(String studentId) {
        return Pattern.matches("^20[0-9]{8}$", studentId);
    }

    public static Boolean isEmail(String email) {
        return Pattern.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", email);
    }
}
