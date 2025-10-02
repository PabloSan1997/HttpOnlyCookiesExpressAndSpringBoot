package com.hola.a.todos.servicio.service;


import com.hola.a.todos.servicio.models.UserDetailsDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JwtService {
    @Value("${mira.babosa}")
    private String secretKey;

    private SecretKey secretKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
    }

    public String getToken(UserDetailsDto userDetailsDto) {
        String username = userDetailsDto.getUsername();
        String authoritie = userDetailsDto.getAuthorities().stream()
                .findFirst().get().getAuthority();
        Claims claims = Jwts.claims().add("authority", authoritie).build();
        return Jwts.builder().signWith(secretKey())
                .subject(username)
                .claims(claims)
                .expiration(new Date(System.currentTimeMillis()+1000*60*60))
                .issuedAt(new Date()).compact();
    }

    public UserDetailsDto validate(String token){
        Claims claims = Jwts.parser().verifyWith(secretKey()).build()
                .parseSignedClaims(token).getPayload();
        String username = claims.getSubject();
        String authority = (String) claims.get("authority");
        var res = new UserDetailsDto();
        res.setUsername(username);
        res.setAuthorityAsString(authority);
        return res;
    }
}
