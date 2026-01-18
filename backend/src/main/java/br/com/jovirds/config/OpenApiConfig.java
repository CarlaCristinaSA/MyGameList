package br.com.jovirds.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    OpenAPI customOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("REST API's RESTful")
                        .version("v1")
                        .description("Creating a RESTful API")
                        .license(new License()
                                .name("Apache 2.0")
                        ));
    }

}
