package club.c1sec.c1ctfplatform;

import club.c1sec.c1ctfplatform.utils.ConnectionWaitUtil;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        ConnectionWaitUtil.waitForDatabase();
        return application.sources(C1ctfPlatformApplication.class);
    }
}
