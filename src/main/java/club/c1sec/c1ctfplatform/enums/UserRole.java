package club.c1sec.c1ctfplatform.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum UserRole {
    USER_ROLE_ADMIN(0), USER_ROLE_STUDENT(1), USER_ROLE_PLAIN(2);

    int id;

    UserRole(int id) {
        this.id = id;
    }

    @JsonValue
    int getId() {
        return id;
    }

    @JsonCreator
    static LogEvent fromId(int id) {
        return Stream.of(LogEvent.values()).filter(userRole -> userRole.id == id).findFirst().orElse(null);
    }

    public static UserRole int2UserRole(Integer l) {
        if (l == null) {
            return null;
        }
        switch (l) {
            case 0:
                return USER_ROLE_ADMIN;
            case 1:
                return USER_ROLE_STUDENT;
            default:
                return USER_ROLE_PLAIN;
        }
    }
}
