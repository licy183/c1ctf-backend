package club.c1sec.c1ctfplatform.vo.container;

import club.c1sec.c1ctfplatform.enums.ContainerStatus;
import lombok.Data;

@Data
public class DeleteContainerResp {
    private ContainerStatus status;
}
