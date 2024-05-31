package club.c1sec.c1ctfplatform.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ContainerHttpService extends BaseHttpService{
    @Autowired
    public ContainerHttpService(@Value("${container.proxy-enabled}") boolean proxyEnabled,
                                @Value("${container.proxy-host}") String proxyHost,
                                @Value("${container.proxy-port}") Integer proxyPort) {
        super(proxyEnabled, proxyHost, proxyPort);
    }
}
