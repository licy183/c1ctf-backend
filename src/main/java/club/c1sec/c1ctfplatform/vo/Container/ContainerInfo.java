package club.c1sec.c1ctfplatform.vo.Container;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ContainerInfo {
    @JsonProperty("expire")
    private Long expireTime;

    // 是否已经创建完成，false表示环境仍在创建中
    @JsonProperty("is_created")
    private Boolean isCreated;

    // 是否已经申请环境，false该题目还需要申请环境
    @JsonProperty("is_allocated")
    private Boolean isAllocated;

    @JsonProperty("host")
    private String host;

    @JsonProperty("port")
    private Integer port;
}
