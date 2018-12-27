package co.com.bancodebogota.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
public class CorssConfiguration {
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
	    CorsConfiguration configuration = new CorsConfiguration();
	    configuration.addAllowedOrigin("*");
	    configuration.setAllowCredentials(true);
	    configuration.addAllowedHeader("*");
	    configuration.addAllowedMethod("OPTIONS");
	    configuration.addAllowedMethod("GET");
	    configuration.addAllowedMethod("POST");
	    configuration.addAllowedMethod("PUT");
	    configuration.addAllowedMethod("DELETE");
	    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();        
	    source.registerCorsConfiguration("/**", configuration);
	    return source;
	}

}