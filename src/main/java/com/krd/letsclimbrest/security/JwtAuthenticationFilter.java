package com.krd.letsclimbrest.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 *
 * This class is used to define a new security filter for checking if there is a JWT in the request header
 * This is part of the security filter chain because we must check for the JWT as part of the authentication process BEFORE we allow access to our API
 *
 * To define this filter we are extending the OncePerRequestFilter class which requires us to override the doFilterInternal method
 */
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    JwtProvider tokenProvider;

    @Autowired
    CustomUserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // Get the JWT from the requests Authoirzation header
        String token = getJwtFromRequest(request);

        // If there was a token and it is valid
        if(StringUtils.hasText(token) && tokenProvider.validateToken(token)){

            // Get the username from our JWT
            String username = tokenProvider.getUsernameFromJwt(token);

            // Use our userDetailsService to load the UserDetails object
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getAuthorities());

            // Set the details of our authenticationToken
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            // Add the authenticationToken to our security context
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }

        // Execute our filter chain and then move on to the next filter
        filterChain.doFilter(request, response);
    }

    // This method looks at the HTTP Request Headers to retrieve the JWT
    private String getJwtFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");

        // If a bearer token exists in the requests Authorization header
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
            // Return only the JWT part of the string
            return bearerToken.substring(7, bearerToken.length());
        }

        // Otherwise return null if there was no bearer token in the requests Authorization header
        return null;
    }
}
