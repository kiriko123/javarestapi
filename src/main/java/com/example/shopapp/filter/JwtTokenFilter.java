package com.example.shopapp.filter;

import com.example.shopapp.component.JwtTokenUtil;
import com.example.shopapp.model.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    @Value("${api.prefix}")
    private String apiPrefix;

    private final UserDetailsService userDetailsService;

    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            if (isBypassTokens(request)) {
                filterChain.doFilter(request, response);
                return;
            }
            final String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                final String token = authHeader.substring(7);
                final String phoneNumber =  jwtTokenUtil.extractPhoneNumber(token);
                if (phoneNumber != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    User userDetails = (User) userDetailsService.loadUserByUsername(phoneNumber);
                    if(jwtTokenUtil.validateToken(token, userDetails)) {
                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                    filterChain.doFilter(request, response);
                }
            }
        }catch (Exception e) {
            // Handle the case where JWT is malformed or cannot be deserialized
            System.err.println("Invalid JWT token: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
        }
    }

    private boolean isBypassTokens(@NonNull HttpServletRequest request) {
        if(request.getServletPath().contains("/swagger-ui") || request.getServletPath().contains("/v3/api-docs") ||
                request.getServletPath().contains("/webjars")){
            return true;
        }
        final List<Pair<String, String>> bypassTokens = Arrays.asList(
                /*Pair.of(String.format("%s/products", apiPrefix), "GET"),
                Pair.of(String.format("%s/categories", apiPrefix), "GET"),*/
                Pair.of(String.format("%s/users/register", apiPrefix), "POST"),
                Pair.of(String.format("%s/users/login", apiPrefix), "POST")
        );
        for (Pair<String, String> token : bypassTokens) {
            if (request.getServletPath().contains(token.getFirst()) && request.getMethod().equals(token.getSecond())) {
                return true;
            }
        }
        return false;
    }
}
