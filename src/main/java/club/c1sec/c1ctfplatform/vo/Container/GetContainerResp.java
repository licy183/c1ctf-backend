package club.c1sec.c1ctfplatform.vo.container;

import club.c1sec.c1ctfplatform.enums.ContainerStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

@Data
public class GetContainerResp {
    private ContainerStatus status;

    private Integer id;

    private String host;

    private String port;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
    private java.time.Instant expire;
}
