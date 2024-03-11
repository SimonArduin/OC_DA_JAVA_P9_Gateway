package com.medilabo.gateway;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
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

    /**
     * This method defines three different users, with different roles.
     * @return
     */
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
                .roles("PRACTITIONER")
                .build();
        UserDetails predictionMicroservice = users
                .username("predictionMicroservice")
                .password("password")
                .roles("PREDICTIONMICROSERVICE")
                .build();
        return new MapReactiveUserDetailsService (planner, practitioner, predictionMicroservice);
    }

    /**
     * This method handles authorization on http requests.
     * When a request is received by the application, it will pass through a filter.
     * If the request fits the filter's criteria, it will go through
     * and be processed by the routes defined in application.yml.
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityWebFilterChain securityFilterChain (ServerHttpSecurity http) throws Exception {
        http
                .authorizeExchange((authorize) -> authorize
                        // get requests on patient authorized for all
                        .pathMatchers(HttpMethod.GET, "/patient/**")
                        .hasAnyRole("PLANNER","PRACTITIONER","PREDICTIONMICROSERVICE")
                        // any other request on patient authorized for planner
                        .pathMatchers("/patient/**")
                        .hasAnyRole("PLANNER")
                        // get requests on note authorized for practitioner and prediction microservice
                        .pathMatchers(HttpMethod.GET, "/note/**")
                        .hasAnyRole("PRACTITIONER","PREDICTIONMICROSERVICE")
                        // any other request on note authorized for practitioner
                        .pathMatchers("/note/**")
                        .hasAnyRole("PRACTITIONER")
                        // get requests on prediction authorized for practitioner
                        .pathMatchers(HttpMethod.GET, "/prediction/**")
                        .hasAnyRole("PRACTITIONER")
                        // deny all other requests
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
