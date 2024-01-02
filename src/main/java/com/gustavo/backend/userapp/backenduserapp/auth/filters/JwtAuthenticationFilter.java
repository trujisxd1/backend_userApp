package com.gustavo.backend.userapp.backenduserapp.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavo.backend.userapp.backenduserapp.auth.TokenConfig;
import com.gustavo.backend.userapp.backenduserapp.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.gustavo.backend.userapp.backenduserapp.auth.TokenConfig.SECRET_KEY;

public class JwtAuthenticationFilter  extends UsernamePasswordAuthenticationFilter {


    private AuthenticationManager  authenticationManager;
    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {

        this.authenticationManager=authenticationManager;
    }
    // Método para intentar la autenticación del usuario
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        User user = null;
        String username=null;
        String password =null;

        try {
            // Intenta leer el objeto User desde el cuerpo de la solicitud
            user=new  ObjectMapper().readValue(request.getInputStream(), User.class);
            username = user.getUsername();
            password=user.getPassword();
            logger.info("Username desde request inputSteam" + username);

            logger.info("Paswword desde request inputSteam" + password);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        // Crea un token de autenticación basado en nombre de usuario y contraseña
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(username,password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override// Método que se ejecuta cuando la autenticación no tiene éxito
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        // Construye una respuesta JSON de error

Map<String,Object> body = new HashMap<>();

body.put("message","error en la autenticacion  username o password incorrecto");
body.put("error",failed.getMessage());

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));
        response.setStatus(401);// Establece el estado de respuesta en no autorizado
        response.setContentType("application/json");



    }

    @Override // Método que se ejecuta cuando la autenticación tiene éxito
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

            String username =((org.springframework.security.core.userdetails.User) authResult.getPrincipal())
                    .getUsername();

            Collection <? extends GrantedAuthority>roles= authResult.getAuthorities();
            boolean isAdmin=roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_ADMIN"));

        Claims claims  =Jwts.claims();
        claims.put("authorities", new ObjectMapper().writeValueAsString(roles));
        claims.put("isAdmin", isAdmin);
        claims.put("username", username);
            String token= Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)   // Establece el sujeto (nombre de usuario) en el token.
                    .signWith(SECRET_KEY) // Firma el token con una clave secreta (SECRET_KEY)
                    .setIssuedAt(new Date())  // Establece la fecha de emisión del token.
                    .setExpiration(new Date(new Date().getTime() + 360000))// Establece la fecha de caducidad del token.
                    .compact();
// Agrega el token JWT en el encabezado "Authorization" de la respuesta
            response.addHeader(TokenConfig.HEADER_AUTHORIZATION,TokenConfig.PREFIX_TOKEN + token);
        // Construye una respuesta JSON de éxito

        Map<String,Object> body = new HashMap<>();

        body.put("token", token);
        body.put("message","hola has iniciado sesion con exito");
        body.put("username" , username);

        response.getWriter().write(new ObjectMapper().writeValueAsString(body));

        response.setStatus(200);
        response.setContentType("application/json");






    }
}
