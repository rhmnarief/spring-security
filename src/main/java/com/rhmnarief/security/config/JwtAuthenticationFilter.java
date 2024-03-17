package com.rhmnarief.security.config;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.micrometer.common.lang.NonNull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.rhmnarief.security.repository.TokenRepository;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;

    // TODO:: Make function for filter JWT
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws ServletException, IOException {
        // Init final variable and get autHeader token from header name Authorization
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;
        // Check if authHeader null or prefix of header not start with Bearer
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }
        // Substring header form keyword 'Bearer '
        jwt = authHeader.substring(7);
        // Get user email with using jwt service extract username
        userEmail = jwtService.extractUsername(jwt);

        // Check if user email not null and security context null
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Get user details
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
            // check valid token using token repository and map it to check if token not
            // expired or not revoke
            var isTokenValid = tokenRepository.findByToken(jwt)
                    .map(t -> !t.isExpired() && !t.isRevoked())
                    .orElse(false);
            // check if is token valid
            if (jwtService.isTokenValid(jwt, userDetails) && isTokenValid) {
                // generate auth token
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                // build auth token to SecurityContextHolder
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

}
