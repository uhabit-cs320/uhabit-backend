package edu.zoomass.uhabit.backend.security;

import edu.zoomass.uhabit.backend.user.UserProfile;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;

public class FirebaseAuthenticationFilter extends OncePerRequestFilter {

    private final FirebaseJwtDecoder jwtDecoder;

    public FirebaseAuthenticationFilter(FirebaseJwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String token = authorizationHeader.substring(7);
                UserProfile user = jwtDecoder.verifyTokenAndGetUser(token);

                FirebaseAuthenticationToken authentication =
                        new FirebaseAuthenticationToken(user, token, Collections.emptyList());

                System.out.println("FirebaseAuthenticationFilter.doFilterInternal: " + user.getEmail());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (Exception e) {
                logger.error("Could not authenticate token", e);
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}