package com.krd.letsclimbrest.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 *
 * This class is used to define a new security filter for intercepting all our requests and checking if there is a JWT in the request header
 * This is part of the security filter chain because we must check for the JWT as part of the authentication process BEFORE we allow access to our API
 *
 * To define this filter we are extending the OncePerRequestFilter class which requires us to override the doFilterInternal method
 */
@Component // This tells spring we want this class to be a managed Spring Bean
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
//    @Autowired
//    JwtProvider tokenProvider;
//
    @Autowired
    CustomUserDetailsService userDetailsService;

    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {

        // Get the authorization header from the intercepted request
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // If the authorization header is either null or doesn't contain a bearer token we want to return early
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        // Extract only the token part from the auth header
        jwt = authHeader.substring(7);

        // Once we have the token, we want to extract the username from the token
        username = jwtService.extractUsername(jwt);

        // If the username from our token is not null and the user is not already authenticated in our Security Context
        if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){

            // Get the user from our database using the username from the JWT
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Use our JwtService class to verify that the JWT is valid
            if(jwtService.isTokenValid(jwt, userDetails)){

                // Update the Security Context
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Execute our filter
        filterChain.doFilter(request, response);

    }
}
