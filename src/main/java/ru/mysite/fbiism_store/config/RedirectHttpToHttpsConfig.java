package ru.mysite.fbiism_store.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@Order(1)
public class RedirectHttpToHttpsConfig {

    @Bean
    public SecurityFilterChain redirectHttpToHttpsSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .requiresChannel(channel -> channel
                        .anyRequest().requiresSecure()); // Перенаправление всех запросов на HTTPS
        return http.build();
    }
}
