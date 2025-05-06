package com.zosh.config;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Service
public class JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtProvider.class);

    // Secure secret key (should be 32+ characters long)
    private static final String SECRET = "mySuperSecureSecretKeyWithAtLeast32Characters";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes(StandardCharsets.UTF_8));

    // Generate JWT Token
    public String generateToken(Authentication auth) {
        String roles = populateAuthorities(auth.getAuthorities());

        return Jwts.builder()
                .setSubject(auth.getName()) // Store email in the subject
                .claim("authorities", roles) // Store roles
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 86400000)) // 24-hour expiration
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Extract email from JWT Token
    public String getEmailFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getSubject(); // Use subject instead of claim("email")

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

    // Validate JWT Token
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException e) {
            logger.error("Invalid JWT Token: {}", e.getMessage());
            return false;
        }
    }

    // Extract roles from JWT
    public List<String> getAuthoritiesFromJwtToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        String authorities = claims.get("authorities", String.class);
        return authorities != null ? Arrays.asList(authorities.split(",")) : new ArrayList<>();
    }

    // Convert authorities to a comma-separated string
    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> auths = new HashSet<>();
        for (GrantedAuthority authority : authorities) {
            auths.add(authority.getAuthority());
        }
        return String.join(",", auths);
    }
}
