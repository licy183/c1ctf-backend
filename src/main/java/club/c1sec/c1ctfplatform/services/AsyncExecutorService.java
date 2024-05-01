package club.c1sec.c1ctfplatform.services;

import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

@Service
public class AsyncExecutorService extends ThreadPoolTaskExecutor {
}
