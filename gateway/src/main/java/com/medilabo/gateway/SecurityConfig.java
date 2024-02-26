package com.medilabo.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public MapReactiveUserDetailsService userDetailsService() {
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        UserDetails user = users
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new MapReactiveUserDetailsService (user);
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain (ServerHttpSecurity http) throws Exception {
        http
                .authorizeExchange((authorize) -> authorize
                        .pathMatchers("/patient/**").permitAll()
                        .pathMatchers("/note/**").authenticated()
                        .anyExchange().denyAll()
                )
                .httpBasic(withDefaults())
                .formLogin(withDefaults());
        return http.build();
    }
}
