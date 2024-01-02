package com.gustavo.backend.userapp.backenduserapp.auth;

import com.gustavo.backend.userapp.backenduserapp.auth.filters.JwtAuthenticationFilter;
import com.gustavo.backend.userapp.backenduserapp.auth.filters.JwtValidationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;

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
                .requestMatchers(HttpMethod.GET, "/api/{id}").hasAnyRole("USER","ADMIN")
                .requestMatchers(HttpMethod.POST,"/api").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE,"/api/{id}").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT,"/api/{id}").hasRole("ADMIN")
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
                .sessionManagement(managment -> managment.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .cors(cors-> cors.configurationSource(corsConfigurationSource()))
                .build();
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

    @Bean
    CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration config= new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
        config.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE"));
        config.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source= new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", config);

        return source;

    }

    @Bean
    FilterRegistrationBean<CorsFilter>corsfilter(){

        FilterRegistrationBean<CorsFilter>bean = new FilterRegistrationBean<>(new CorsFilter(corsConfigurationSource()));
        bean.setOrder(Ordered.HIGHEST_PRECEDENCE);
        return bean;
    }
}
