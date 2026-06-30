package com.exe.buddy_english_be.security;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.exe.buddy_english_be.modules.user.entity.User;
import com.exe.buddy_english_be.modules.user.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProvider jwtProvider;
    private final CookieUtil cookieUtil;
    private final UserRepository userRepository;

    public JwtAuthenticationFilter(
            JwtProvider jwtProvider,
            CookieUtil cookieUtil,
            UserRepository userRepository
    ) {
        this.jwtProvider = jwtProvider;
        this.cookieUtil = cookieUtil;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {
        String token = cookieUtil.getAccessToken(request);

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null
                && jwtProvider.isTokenValid(token)) {
            Long userId = jwtProvider.getUserId(token);
            userRepository.findByIdWithRoles(userId).ifPresent(this::authenticate);
        }

        filterChain.doFilter(request, response);
    }

    @SuppressWarnings("unchecked")
    private void authenticate(User user) {
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> {
                    String roleName = role.getName();
                    return roleName.startsWith("ROLE_") ? roleName : "ROLE_" + roleName;
                })
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(user.getId(), null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
