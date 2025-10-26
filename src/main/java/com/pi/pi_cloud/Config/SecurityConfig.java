package com.pi.pi_cloud.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Desactiva CSRF (para permitir peticiones POST JSON sin token)
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/h2-console/**") // Desactiva CSRF para H2
                        .disable()
                )
                .headers(headers -> headers
                        .frameOptions(frame -> frame.disable()) // Permite iframes
                )


                // Define qué endpoints son públicos
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/**",
                                "/api/auth/login",
                                "/api/auth/register",
                                "/api/auth/qr/**",
                                "/login",
                                "/h2-console/**",
                                "/css/**",
                                "/dashboard/**",
                                "/users/**",
                                "/departamentos/**",
                                "/eliminardep/**",
                                "/dep/guardar/**",
                                "/js/**"
                        ).permitAll()
                        .anyRequest().authenticated() // el resto requiere sesión
                )

                // Indica que usas autenticación basada en sesión (no formulario de Spring)
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable()) // desactiva login básico

                // Usa la sesión HTTP para recordar al usuario autenticado
                .sessionManagement(session -> session
                        .maximumSessions(1) // 1 sesión activa por usuario
                )

                // Permite logout personalizado
                .logout(logout -> logout
                        .logoutUrl("/api/auth/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                );

        return http.build();
    }
}
