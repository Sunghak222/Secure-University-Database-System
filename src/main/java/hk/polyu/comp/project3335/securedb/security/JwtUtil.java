package hk.polyu.comp.project3335.securedb.security;

import hk.polyu.comp.project3335.securedb.model.AuthUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
@Component
public class JwtUtil {

    private final Key key = Keys.hmacShaKeyFor(
            "ThisIsASecretKeyForCOMP3335Project!!".getBytes(StandardCharsets.UTF_8)
    );

    private final long EXPIRATION = 1000 * 60 * 60; // 1hour

    // Generate token
    public String generateToken(AuthUser user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())
                .claim("studentId", user.getStudentId())
                .claim("guardianId", user.getGuardianId())
                .claim("staffId", user.getStaffId())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }


    // Resolve header token
    public String resolveToken(HttpServletRequest request) {
        String bearer = request.getHeader("Authorization");

        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        return null;
    }

    // Parse claims
    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // Extract all claims
    public Claims extractAllClaims(String token) {
        return parseClaims(token);
    }

    // Extract email
    public String getEmail(String token) {
        return parseClaims(token).getSubject();
    }

    // Extract role
    public String getRole(String token) {
        return (String) parseClaims(token).get("role");
    }

    // Extract studentId
    public Long extractStudentId(HttpServletRequest request) {
        String token = resolveToken(request);
        if (token == null) return null;

        Claims claims = extractAllClaims(token);
        return claims.get("studentId", Long.class);
    }

    public Long extractGuardianId(HttpServletRequest request) {
        String token = resolveToken(request);
        if (token == null) return null;

        Claims claims = parseClaims(token);
        return claims.get("guardianId", Long.class);
    }
    // Validate token
    public boolean validate(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (JwtException e) {
            return false;
        }
    }
}
