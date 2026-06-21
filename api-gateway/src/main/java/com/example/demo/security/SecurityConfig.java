package com.example.demo.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import reactor.core.publisher.Mono;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        // ✔ correct reactive authentication manager
        ReactiveAuthenticationManager authenticationManager =
                authentication -> Mono.just(authentication);

        AuthenticationWebFilter authenticationWebFilter =
                new AuthenticationWebFilter(authenticationManager);

        authenticationWebFilter.setServerAuthenticationConverter(jwtAuthenticationFilter);

        authenticationWebFilter.setSecurityContextRepository(
                NoOpServerSecurityContextRepository.getInstance()
        );

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)

                .securityContextRepository(
                        NoOpServerSecurityContextRepository.getInstance()
                )

                .authorizeExchange(exchange -> exchange
                		// public
                        .pathMatchers("/auth-service/api/auth/**").permitAll()

                        // only USER
                        .pathMatchers("/customer-service/**")
                        .hasRole("USER")

                        // only ADMIN
                        .pathMatchers("/loan-service/**")
                        //.hasRole("ADMIN")
						.hasRole("USER")

                        .anyExchange().authenticated()
                )

                .addFilterAt(authenticationWebFilter, SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
