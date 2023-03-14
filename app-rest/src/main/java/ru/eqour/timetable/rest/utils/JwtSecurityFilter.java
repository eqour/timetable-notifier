package ru.eqour.timetable.rest.utils;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.eqour.timetable.rest.service.auth.JwtService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

public class JwtSecurityFilter extends OncePerRequestFilter {

    private final JwtService jwtService;

    public JwtSecurityFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null) {
            try {
                String token = authHeader.substring(7);
                String email = jwtService.getClaims(token).getSubject();
                SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(email, null, Collections.emptyList()));
            } catch (Exception ignored) {
            }
        }
        filterChain.doFilter(request, response);
    }
}
