package club.c1sec.c1ctfplatform.vo.user;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CasLoginRequest {
    @NotBlank
    private String ticket;
}
