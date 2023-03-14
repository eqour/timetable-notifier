package ru.eqour.timetable.rest.service.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.time.Duration;
import java.util.Date;

@Service
public class JwtService {

    private final Key secret;

    public JwtService() {
        secret = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    }

    public String generateToken(String email) {
        Date now = new Date();
        Date expDate = new Date(now.getTime() + Duration.ofDays(1).toMillis());
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expDate)
                .setSubject(email)
                .signWith(secret)
                .compact();
    }

    public Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secret)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
