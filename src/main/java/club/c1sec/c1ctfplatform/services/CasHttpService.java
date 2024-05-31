package club.c1sec.c1ctfplatform.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CasHttpService extends BaseHttpService {
    @Autowired
    public CasHttpService(@Value("${oauth.proxy-enabled}") boolean proxyEnabled,
                          @Value("${oauth.proxy-host}") String proxyHost,
                          @Value("${oauth.proxy-port}") Integer proxyPort) {
        super(proxyEnabled, proxyHost, proxyPort);
    }
}
