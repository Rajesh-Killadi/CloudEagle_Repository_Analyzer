package com.cloudeagle.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.client.RestTemplate;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .anyRequest().authenticated()
            )
//            .oauth2Login(Customizer.withDefaults()).defaultSuccessUrl("/swagger-ui/index.html", true);
            .oauth2Login(oauth -> oauth
                    .defaultSuccessUrl("/swagger-ui/index.html", true)
                );

        return http.build();
    }
    
    @Bean
    public RestTemplate restTemplate() {
    	
    	return new RestTemplate();
    }
}
