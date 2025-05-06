package com.zosh.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class JWTTokenValidator extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JWTTokenValidator.class);

    // Use the same secret key as JwtProvider
    private static final String SECRET = "mySuperSecureSecretKeyWithAtLeast32Characters";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = request.getHeader("Authorization");

        if (jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7); // Remove "Bearer " prefix

            try {
                Claims claims = Jwts.parserBuilder()
                        .setSigningKey(key)
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody();

                String email = claims.getSubject(); // Use subject instead of claim("email")
                String authorities = claims.get("authorities", String.class);

                List<GrantedAuthority> auths = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);

                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, auths);
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (ExpiredJwtException e) {
                logger.error("JWT Token expired: {}", e.getMessage());
                throw new BadCredentialsException("JWT Token has expired", e);
            } catch (UnsupportedJwtException e) {
                logger.error("Unsupported JWT Token: {}", e.getMessage());
                throw new BadCredentialsException("Unsupported JWT Token", e);
            } catch (MalformedJwtException e) {
                logger.error("Malformed JWT Token: {}", e.getMessage());
                throw new BadCredentialsException("Malformed JWT Token", e);
            } catch (SignatureException e) {
                logger.error("Invalid JWT signature: {}", e.getMessage());
                throw new BadCredentialsException("Invalid JWT signature", e);
            } catch (IllegalArgumentException e) {
                logger.error("JWT Token is null or empty: {}", e.getMessage());
                throw new BadCredentialsException("JWT Token is invalid", e);
            }
        }

        filterChain.doFilter(request, response);
    }
}
