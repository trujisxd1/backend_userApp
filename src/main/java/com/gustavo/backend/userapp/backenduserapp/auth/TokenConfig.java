package com.gustavo.backend.userapp.backenduserapp.auth;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import java.security.Key;

public class TokenConfig {
    /**
     * Clave secreta utilizada para firmar y verificar tokens JWT.
     * Se inicializa con una clave generada automáticamente utilizando el algoritmo HMAC con SHA-256 (HS256).
     * Esta clave es fundamental para la seguridad de los tokens JWT, y debe mantenerse segura y privada.
     * No se debe exponer en el código fuente ni en repositorios públicos.
     */
    public final static Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
    //public final  static  String SECRET_KEY="algun_token_con_frase_secreta.";

    public final static String PREFIX_TOKEN = "Bearer ";

    public final static String HEADER_AUTHORIZATION = "Authorization";


}