package club.c1sec.c1ctfplatform.utils;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerUI {
    @Bean
    public OpenAPI createRestApi() {
        return new OpenAPI().info(apiInfo());
    }

    private Info apiInfo() {
        return new Info().title("API Test").description("C1CTF").version("1.0.1");
    }
}
