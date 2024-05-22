package club.c1sec.c1ctfplatform.vo.container;

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

    // 已经format的容器访问url如 nc xxx 1234 http://xxx:1234/xxx
    @JsonProperty("url")
    private String url;
}
