package ru.eqour.timetable.rest.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    private final Key secret;

    public JwtService() {
        secret = Keys.secretKeyFor(SignatureAlgorithm.HS512);
        // todo export secret to properties
        System.out.println("generated secret: " + Encoders.BASE64.encode(secret.getEncoded()));
    }

    public String generateToken(String email) {
        long period = 1000 * 60 * 60 * 24;
        Date now = new Date();
        Date expDate = new Date(now.getTime() + period);
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expDate)
                .setSubject(email)
                .signWith(secret)
                .compact();
    }
}
