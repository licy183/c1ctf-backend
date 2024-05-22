package club.c1sec.c1ctfplatform.utils;

import lombok.experimental.UtilityClass;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

@UtilityClass
public class StringUtil {
    public static String notNull(String string) {
        return Objects.requireNonNullElse(string, "");
    }

    public static Boolean isStudentId(String studentId) {
        return Pattern.matches("^20[0-9]{8}$", studentId);
    }

    public static Boolean isEmail(String email) {
        return Pattern.matches("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$", email);
    }
}
