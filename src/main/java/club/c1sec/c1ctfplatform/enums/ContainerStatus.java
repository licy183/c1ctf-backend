package club.c1sec.c1ctfplatform.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.stream.Stream;

public enum ContainerStatus {
    PROCESS_ERROR(-1),
    CREATING(0),
    CREATED(1),
    ERROR(2),
    DELETED(3);

    int id;

    ContainerStatus(int id) {
        this.id = id;
    }

    @JsonValue
    public int getId() {
        return id;
    }

    @JsonCreator
    public static ContainerStatus fromId(int id) {
        return Stream.of(ContainerStatus.values()).filter(event -> event.id == id).findFirst().orElse(null);
    }
}
