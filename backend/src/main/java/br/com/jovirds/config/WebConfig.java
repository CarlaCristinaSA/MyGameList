package br.com.jovirds.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private static final Logger logger = LoggerFactory.getLogger(WebConfig.class);

    @Value("${cors.originPatterns:default}")
    private String corsOriginPatterns = "";

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        var allowedOrigins = Arrays.stream(corsOriginPatterns.split(","))
                .map(String::trim)
                .toArray(String[]::new);

        logger.info("=== CORS Configuration ===");
        logger.info("corsOriginPatterns: {}", corsOriginPatterns);
        logger.info("Parsed origins: {}", Arrays.toString(allowedOrigins));

        registry.addMapping("/**")
                .allowedOriginPatterns(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
        
        logger.info("CORS registered with max age 3600");
    }

    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {

        // Query Paramether
        /*
        configurer.favorParameter(true)
                .parameterName("mediaType")
                .ignoreAcceptHeader(true)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML);


         */
        //Via Header
        configurer.favorParameter(true)
                .parameterName("mediaType")
                .ignoreAcceptHeader(false)
                .useRegisteredExtensionsOnly(false)
                .defaultContentType(MediaType.APPLICATION_JSON)
                .mediaType("json", MediaType.APPLICATION_JSON)
                .mediaType("xml", MediaType.APPLICATION_XML)
                .mediaType("yaml", MediaType.APPLICATION_YAML);

    }
}
