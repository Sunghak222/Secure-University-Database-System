package hk.polyu.comp.project3335.securedb.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    // Changed: secret key for signing
    private final Key key = Keys.hmacShaKeyFor("ThisIsASecretKeyForCOMP3335Project!!".getBytes());

    private final long EXPIRATION = 1000 * 60 * 60; // 1 hour

    // Changed: Generate JWT
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    // Changed: extract email
    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    // Changed: extract role
    public String getRole(String token) {
        return (String) parseClaims(token).get("role");
    }

    // Changed: token validation
    public boolean validate(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}