package com.lava.bakery.security;

import com.lava.bakery.entity.User;
import com.lava.bakery.repository.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    public JwtFilter(JwtUtil jwtUtil, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getServletPath();
        if (request.getMethod().equals("OPTIONS")) {
            filterChain.doFilter(request, response);
            return;
        }
        // ✅ SKIP PUBLIC APIs (FAST EXIT)
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String header = request.getHeader("Authorization");

            // ✅ No token → allow request
            if (header == null || !header.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            String token = header.substring(7);

            // ✅ Validate token
            if (!jwtUtil.validateToken(token)) {
                filterChain.doFilter(request, response);
                return;
            }
            String username = jwtUtil.extractEmail(token);
            String role = jwtUtil.extractRole(token);

            if (SecurityContextHolder.getContext().getAuthentication() == null) {

                if (role.equals("ROLE_USER") || role.equals("ROLE_ADMIN")) {

                    // ✅ USER / ADMIN flow
                    User user = userRepository.findByEmail(username).orElse(null);

                    if (user != null) {
                        UsernamePasswordAuthenticationToken auth =
                                new UsernamePasswordAuthenticationToken(
                                        username,
                                        null,
                                        List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName()))
                                );

                        SecurityContextHolder.getContext().setAuthentication(auth);
                    }

                } else if (role.equals("ROLE_DELIVERY")) {

                    // ✅ DELIVERY flow
                    UsernamePasswordAuthenticationToken auth =
                            new UsernamePasswordAuthenticationToken(
                                    username,
                                    null,
                                    List.of(new SimpleGrantedAuthority(role))
                            );

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
            // ❗ Never break request flow
            System.out.println("JWT Error: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    // 🔥 CLEAN METHOD (BEST PRACTICE)
    private boolean isPublicPath(String path) {
        return path.startsWith("/api/auth") ||
//                path.startsWith("/api/cakes") ||
//                path.startsWith("/api/orders") ||
                path.startsWith("/api/delivery/login") ||
                path.startsWith("/images") ||
                path.startsWith("/uploads") ||
                path.equals("/");
    }
}