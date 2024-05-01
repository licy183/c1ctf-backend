package club.c1sec.c1ctfplatform;

import club.c1sec.c1ctfplatform.utils.ConnectionWaitUtil;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class C1ctfPlatformApplication {
    public static void main(String[] args) {
        ConnectionWaitUtil.waitForDatabase();
        SpringApplication.run(C1ctfPlatformApplication.class, args);
    }
}
