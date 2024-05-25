package club.c1sec.c1ctfplatform.services;

import club.c1sec.c1ctfplatform.utils.StringUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class CasValidateService {

    private final String CAS_REGISTER_STUID_PREFIX = "CAS_REGISTER_STUID_";

    private final String CAS_REGISTER_NAME_PREFIX = "CAS_REGISTER_NAME_";

    private final RedisService redisService;

    private final HttpService httpService;

    private final ConfigService config;

    private final String casServerUrl;

    private final String encodedCasRedirectUrl;

    @Autowired
    public CasValidateService(@Value("${oauth.validate-url}") String casServerUrl,
                              @Value("${oauth.redirect-url}") String casRedirectUrl,
                              RedisService redisService,
                              HttpService httpService,
                              ConfigService config) {
        this.redisService = redisService;
        this.httpService = httpService;
        this.casServerUrl = casServerUrl;
        this.encodedCasRedirectUrl = casRedirectUrl;
        this.config = config;
    }

    public Map.Entry<String, String> casTicketValidate(String ticket) {
        try {
            _CasValidateResult result =
                    httpService.sendGetRequest(casServerUrl, _createTicketValidateForm(ticket), _CasValidateResult.class);
            _ServiceResponse response = result.getServiceResponse();
            if (response.getAuthenticationFailure() != null) {
                log.error("casTicketValidate: ticket {} not valid: {}", ticket, response.getAuthenticationFailure());
                return null;
            }
            return Map.entry(response.getAuthenticationSuccess().getUser(),
                             StringUtil.notNull(response.getAuthenticationSuccess().getAttributes().getCn()[0])
            );
        } catch (Exception e) {
            log.error("casTicketValidate: ticket {} not valid", ticket, e);
            return null;
        }
    }

    public boolean isPreRegisterOpen() {
        return config.getRegisterOpen();
    }

    public String casPreRegister(String stuId, String name) {
        final String uuid = UUID.randomUUID().toString();
        // 5分钟内注册有效
        redisService.setKeyValueWithExpire(CAS_REGISTER_STUID_PREFIX + uuid, stuId, 300);
        redisService.setKeyValueWithExpire(CAS_REGISTER_NAME_PREFIX + uuid, name, 300);
        return uuid;
    }

    public Map.Entry<String, String> getCasPreRegisterInfo(String uuid) {
        final String stuId = redisService.getValue(CAS_REGISTER_STUID_PREFIX + uuid);
        final String name = redisService.getValue(CAS_REGISTER_NAME_PREFIX + uuid);
        if (stuId != null && name != null) {
            return Map.entry(stuId, name);
        }
        return null;
    }

    private Map<String, String> _createTicketValidateForm(String ticket) {
        return Map.of(
                "format", "json",
                "service", encodedCasRedirectUrl,
                "ticket", ticket
        );
    }

    @Data
    private static class _CasValidateResult {
        private _ServiceResponse serviceResponse;
    }

    @Data
    private static class _ServiceResponse {
        private _AuthenticationSuccess authenticationSuccess;
        private _AuthenticationFailure authenticationFailure;
    }

    @Data
    private static class _AuthenticationSuccess {
        private String user;
        private _AuthenticationSuccessAttributes attributes;
    }

    @Data
    private static class _AuthenticationSuccessAttributes {
        private String[] cn;
    }

    @Data
    private static class _AuthenticationFailure {
        private String code;
        private String description;
    }
}
