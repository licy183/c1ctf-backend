package club.c1sec.c1ctfplatform.interceptor;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(newInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public Interceptor newInterceptor() {
        return new Interceptor();
    }
}