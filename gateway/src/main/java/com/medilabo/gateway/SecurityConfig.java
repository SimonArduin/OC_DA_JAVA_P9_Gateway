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
        UserDetails planner = users
                .username("planner")
                .password("password")
                .roles("PLANNER")
                .build();
        UserDetails practitioner = users
                .username("practitioner")
                .password("password")
                .roles("PLANNER", "PRACTITIONER")
                .build();
        UserDetails predictionMicroservice = users
                .username("predictionMicroservice")
                .password("password")
                .roles("READ")
                .build();
        return new MapReactiveUserDetailsService (planner, practitioner, predictionMicroservice);
    }

    @Bean
    public SecurityWebFilterChain securityFilterChain (ServerHttpSecurity http) throws Exception {
        http
                .authorizeExchange((authorize) -> authorize
                        .pathMatchers("/patient/get/**").hasAnyRole("READ","PLANNER")
                        .pathMatchers("/note/getbypatientid/**").hasAnyRole("READ","PRACTITIONER")
                        .pathMatchers("/patient/**").hasRole("PLANNER")
                        .pathMatchers("/note/**","/prediction/**").hasRole("PRACTITIONER")
                        .anyExchange().denyAll()
                )
                .httpBasic(withDefaults())
                .formLogin(withDefaults())
                .cors(withDefaults()).authorizeExchange(withDefaults())
                .csrf( csrf -> csrf.disable())
        ;
        return http.build();
    }
}
