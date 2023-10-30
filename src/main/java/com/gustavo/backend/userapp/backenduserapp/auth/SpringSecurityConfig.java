package com.gustavo.backend.userapp.backenduserapp.auth;

import com.gustavo.backend.userapp.backenduserapp.auth.filters.JwtAuthenticationFilter;
import com.gustavo.backend.userapp.backenduserapp.auth.filters.JwtValidationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SpringSecurityConfig {

    @Autowired
    private AuthenticationConfiguration authenticationConfiguration;

    @Bean
        //cadena de filtro para autorizacion
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {


        return http.authorizeHttpRequests()
                // Autorización para el método GET en la URL /api (cualquiera puede acceder)
                .requestMatchers(HttpMethod.GET, "/api").permitAll()
                // Todas las demás solicitudes requieren autenticación
                .anyRequest()
                .authenticated()
                .and()
                // Agrega el filtro de autenticación JWT
                .addFilter(new JwtAuthenticationFilter(authenticationConfiguration.getAuthenticationManager()))
                // Deshabilita la protección CSRF
                .addFilter(new JwtValidationFilter(authenticationConfiguration.getAuthenticationManager()))
                .csrf(config -> config.disable())
                // Establece la gestión de sesiones en Stateless (sin sesiones)
                .sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS)).build();
    }

    @Bean
    ///enxriptador de contrasñas
    PasswordEncoder passwordEncoder (){

        return new BCryptPasswordEncoder();
    }


    @Bean
    AuthenticationManager authenticationManager() throws  Exception{

        return  authenticationConfiguration.getAuthenticationManager();
    }
}
