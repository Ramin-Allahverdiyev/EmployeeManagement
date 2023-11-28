package com.EmployeeManagement.config;

import com.EmployeeManagement.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.httpBasic(Customizer.withDefaults());
        httpSecurity.csrf(CsrfConfigurer::disable);
        httpSecurity.authorizeHttpRequests(request ->
                        request.
                                requestMatchers(antMatcher("/api-docs/**"))
                                .permitAll()
                                .requestMatchers(antMatcher("/swagger-ui/**"))
                                .permitAll()
                                .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/employee-management/auth/**"))
                                .permitAll()
                                .requestMatchers(antMatcher(HttpMethod.PUT, "/api/v1/employee-management/auth/**"))
                                .hasAnyAuthority("ADMIN","USER")
                                .requestMatchers(antMatcher(HttpMethod.DELETE, "/api/v1/employee-management/auth/**"))
                                .hasAuthority("ADMIN")
                                .requestMatchers(antMatcher(HttpMethod.GET, "/api/v1/employee-management/auth/**"))
                                .hasAuthority("ADMIN")
                                .requestMatchers(antMatcher(HttpMethod.POST, "/api/v1/employee-management/department/**"), antMatcher(HttpMethod.POST, "/api/v1/employee-management/employee/**")
                                        , antMatcher(HttpMethod.POST, "/api/v1/employee-management/position/**"), antMatcher(HttpMethod.POST, "/api/v1/employee-management/role/**"))
                                        .hasAuthority("ADMIN")
                                .requestMatchers(antMatcher(HttpMethod.DELETE, "/api/v1/employee-management/department/**"), antMatcher(HttpMethod.DELETE, "/api/v1/employee-management/employee/**")
                                        , antMatcher(HttpMethod.DELETE, "/api/v1/employee-management/position/**"), antMatcher(HttpMethod.DELETE, "/api/v1/employee-management/role/**"))
                                .hasAuthority("ADMIN")
                                .requestMatchers(antMatcher(HttpMethod.PUT, "/api/v1/employee-management/department/**"), antMatcher(HttpMethod.PUT, "/api/v1/employee-management/employee/**")
                                        , antMatcher(HttpMethod.PUT, "/api/v1/employee-management/position/**"), antMatcher(HttpMethod.PUT, "/api/v1/employee-management/role/**"))
                                .hasAuthority("ADMIN")
                                .requestMatchers(antMatcher(HttpMethod.GET, "/api/v1/employee-management/department/**"), antMatcher(HttpMethod.GET, "/api/v1/employee-management/employee/**")
                                        , antMatcher(HttpMethod.GET, "/api/v1/employee-management/position/**"), antMatcher(HttpMethod.GET, "/api/v1/employee-management/role/**"))
                                .hasAnyAuthority("ADMIN", "USER")
                                .anyRequest()
                                .authenticated())
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        httpSecurity.headers((headers) -> headers.disable());
        return httpSecurity.build();
    }
}
