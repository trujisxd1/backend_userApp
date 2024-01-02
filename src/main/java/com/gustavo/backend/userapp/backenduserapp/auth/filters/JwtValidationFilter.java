package com.gustavo.backend.userapp.backenduserapp.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavo.backend.userapp.backenduserapp.auth.SimpleGrantedAuthorityJsonCreator;
import com.gustavo.backend.userapp.backenduserapp.auth.TokenConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.*;

import static com.gustavo.backend.userapp.backenduserapp.auth.TokenConfig.SECRET_KEY;

public class JwtValidationFilter  extends BasicAuthenticationFilter {

    public JwtValidationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        // Obtiene el encabezado "Authorization" de la solicitud

        String header=request.getHeader(TokenConfig.HEADER_AUTHORIZATION);

        if (header==null || !header.startsWith(TokenConfig.PREFIX_TOKEN)){
            // Si no se encuentra un token JWT en el encabezado, pasa al siguiente filtro en la cadena

            chain.doFilter(request,response);
            return;
        }
        // Extrae el token JWT de la cabecera y lo decodifica

        String token=header.replace(TokenConfig.PREFIX_TOKEN,"");

       try{
           Claims claims = Jwts.parserBuilder()  // Inicio del proceso de análisis y validación del token.
                   .setSigningKey(SECRET_KEY)       // Se establece la clave secreta (SECRET_KEY) para verificar la firma del token.
                   .build()                        // Se crea el analizador de tokens.
                   .parseClaimsJws(token)          // Se analiza el token JWT y se obtienen las reclamaciones (claims).
                   .getBody();
           Object authoritiesClaim=claims.get("authorities");

           String username =claims.getSubject();// Se obtiene el nombre de usuario (subject) del token.


            Collection<? extends   GrantedAuthority> authorities= Arrays
                    .asList(new ObjectMapper().addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityJsonCreator.class).readValue(authoritiesClaim.toString().getBytes(),SimpleGrantedAuthority[].class));

            // Crea un objeto de autenticación con el nombre de usuario y las autoridades
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username,null,authorities);

            // Establece el objeto de autenticación en el contexto de seguridad
            SecurityContextHolder.getContext().setAuthentication(authentication);

            // Continúa la cadena de filtros
            chain.doFilter(request,response);
        }catch (JwtException e){
            // Si el token no es válido, se devuelve un mensaje de error

            Map<String,String>body = new HashMap<>();

            body.put("error", e.getMessage());

            body.put("message","El token no es valido");
            response.getWriter().write(new ObjectMapper().writeValueAsString(body));
            response.setStatus(403);
            response.setContentType("application/json");
        }

    }
}
