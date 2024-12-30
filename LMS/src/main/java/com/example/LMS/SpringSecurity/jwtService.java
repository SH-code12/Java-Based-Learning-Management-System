package com.example.LMS.SpringSecurity;

import com.example.LMS.models.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class jwtService {

    private static final String SECRET_KEY = "7f4a3ee3b6d9b8e2c3c1b9f1d1e3a5b6b1c3f9a7e2f1b3d8e4f9b6a3e2d4c7b9\n"; // Replace with your secret key

    // Generate JWT Token
    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("authority", user.getRole())  // Changed from "role" to "authority"
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24 hours expiration
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    // Extract username from JWT token
    public String extractUsername(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    // Extract authority from JWT token (instead of role)
    public String extractAuthority(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .get("authority", String.class); // Extracts "authority" instead of "role"
    }

    // Validate the token
    public boolean isTokenValid(String token, UserDetails userDetails) {
        String username = extractUsername(token);
        // Cast userDetails to your custom User class
        User user = (User) userDetails;
        return (username.equals(user.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    private Date extractExpiration(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
    }
}
