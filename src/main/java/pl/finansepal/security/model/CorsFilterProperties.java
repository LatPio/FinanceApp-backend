package pl.finansepal.security.model;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Data
@Validated
@ConfigurationProperties(prefix = CorsFilterProperties.PREFIX)
public class CorsFilterProperties {

    public static final String PREFIX = "web.filter.cors";

    private int order = 100;

    private List<String> urlPatterns;

    private List<String> allowedOrigins;

    private List<String> allowedMethods;

    private List<String> allowedHeaders;

    private List<String> exposedHeaders;

    private Long maxAge;
}
