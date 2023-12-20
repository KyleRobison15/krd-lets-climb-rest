package com.krd.letsclimbrest.security;

import com.krd.letsclimbrest.constants.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * This class is used to generate our JSON Web Tokens
 */

@Component
public class JwtProvider {

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date currentDate = new Date();
        Date tokenExpirationDate = new Date(currentDate.getTime() + SecurityConstants.JWT_EXPIRATION);

        String token = Jwts.builder()
                .subject(username)
                .issuedAt(new Date())
                .expiration(tokenExpirationDate)
                .signWith(SecurityConstants.KEY)
                .compact();

        return token;
    }

    public String getUsernameFromJwt(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(SecurityConstants.KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    // Use a built in JWT parser to validate tokens
    public boolean validateToken(String token){
        try{
            Jwts.parser()
                    .verifyWith(SecurityConstants.KEY)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e){
            throw new AuthenticationCredentialsNotFoundException("JWT was expired or incorrect.");
        }
    }

}
