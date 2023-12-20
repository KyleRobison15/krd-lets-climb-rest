package com.krd.letsclimbrest.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {
    /**
     *
     * This class is used to handle our application's JWTs
     *
     * A JWT is essentially just an encoded message
     *
     * JWT's have three parts:
     *      1. Header -> Contains the metadata for the token including
     *          a. What algo is used for signing the token
     *          b. What type of token it is (ie. JWT)
     *      2. Payload -> Contains the DATA (ie. the message part)
     *          a. The data is also known as "claims" which are statements about an entity typically a User
     *              i. Registered Claims -> Issuer, Subject, Expiration (not required, but recommended)
     *              ii. Public Claims
     *              iii. Private Claims
     *          b. This is where the User information is
     *      3. Signature -> Verifies the sender of the JWT
     */

    // Generated from
    private static final String SECRET_KEY = "524149cdbb00292e7f5f52f68290d55e3ca42597dfc823aa3b942065c8e4d006";


    /**
     *
     * @param extraClaims -> Any additional claims we want to add to the JWT in key value pair format
     *                    for example:  {roles: ["user"]}
     * @param userDetails -> Spring Security's interface for getting user details
     *                    In our case we are implementing a custom user details service for getting user details from our database
     * @return -> the generated JWT as a String
     */

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // Tokens will be valid for 24 hours
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // A simpler way to generate a token when we don't need to add additional claims
    public String generateToken(UserDetails userDetails){
        return generateToken(new HashMap<>(), userDetails);
    }


    /**
     *
     * This method is used to extract the username from a JSON Web Token
     * When our security filter chain intercepts a request, we grab the token and extract the username from it
     *
     * @param token - the JSON Web Token
     * @return - the username encoded in the JSON Web Token
     */
    public String extractUsername(String token) {
        // Pass the Claims.getSubject() method so we can get the "Subject" part of the claim
        return extractClaim(token, Claims::getSubject);
    }

    /**
     *
     * This is a simple way to extract a single claim from a JWT
     *
     * @param token -> The token we want to extract the claim from
     * @param claimsResolver -> A function that returns part of a JWT claim
     * @return The result from the claimsResolver that was provided
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver){
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     *
     * Method for extracting the expiration claim from the token
     *
     * @param token
     * @return -> The exiration date of the given token
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     *
     * Method for checking that the given token actually belongs to the user and that it hasn't expired
     *
     * @param token
     * @param userDetails
     * @return true if the username on the token matches the user's username and the token is not expired
     */
    public boolean isTokenValid(String token, UserDetails userDetails){

        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);

    }

    /**
     *
     * A signing key is a secret used to sign a JWT giving it a unique signature that can be used to verify that:
     *      1. The sender of this request is who it claims to be
     *      2. Ensures the message wasn't changed along the way
     *
     * The signing key is used in conjuction with the signing algo specified in the JWT header to create the signature
     *
     * @param token
     * @return
     */
    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     *
     * Method for checking if the provided token is expired
     *
     * @param token -> The token we are checking for expiration
     * @return -> if the expiration claim from the token is before today's date ? true : false
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }


    // Decodes the signing key
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
