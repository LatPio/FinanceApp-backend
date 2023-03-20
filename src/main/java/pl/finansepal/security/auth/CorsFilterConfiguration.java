package pl.finansepal.security.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import pl.finansepal.security.model.CorsFilterProperties;

@Configuration
@EnableConfigurationProperties(CorsFilterProperties.class)
@RequiredArgsConstructor
public class CorsFilterConfiguration {

    private final CorsFilterProperties corsFilterProperties;
    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration config = buildCorsParameters();
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    private CorsConfiguration buildCorsParameters() {
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.applyPermitDefaultValues();

        if(corsFilterProperties.getMaxAge() !=null){
            corsConfiguration.setMaxAge(corsFilterProperties.getMaxAge());
        }
        if(!CollectionUtils.isEmpty(corsFilterProperties.getAllowedMethods())){
            corsConfiguration.setAllowedMethods(corsFilterProperties.getAllowedMethods());
        }
        if(!CollectionUtils.isEmpty(corsFilterProperties.getAllowedHeaders())){
            corsConfiguration.setAllowedHeaders(corsFilterProperties.getAllowedHeaders());
        }
        if(!CollectionUtils.isEmpty(corsFilterProperties.getAllowedOrigins())){
            corsConfiguration.setAllowedOrigins(corsFilterProperties.getAllowedOrigins());
        }
        if(!CollectionUtils.isEmpty(corsFilterProperties.getExposedHeaders())){
            corsConfiguration.setExposedHeaders(corsFilterProperties.getExposedHeaders());
        }

        return corsConfiguration;
    }
}
