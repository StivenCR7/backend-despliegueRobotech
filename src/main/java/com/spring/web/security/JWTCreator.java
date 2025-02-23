package com.spring.web.security;

import io.jsonwebtoken.*;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import com.spring.web.model.Roles;

public class JWTCreator {
	
	public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String ROLES_AUTHORITIES = "authorities";

    public static String create(String prefix, String key, JWTObject jwtObject) {
        String id = UUID.randomUUID().toString();
        Date now = new Date();
        Date exp = new Date(System.currentTimeMillis() + (1000 * 60 * 30));

     
        List<String> roles = jwtObject.getRoles().stream()
                .map(Roles::getNombre)
                .collect(Collectors.toList());

        String token = Jwts.builder()
                .setId(id)
                .setSubject(jwtObject.getSubject())
                .setIssuedAt(now)
                .setExpiration(exp)
                .claim(ROLES_AUTHORITIES, roles)
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
        return prefix + " " + token;
    }

    public static JWTObject create(String token, String prefix, String key)
            throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException {
        
        JWTObject object = new JWTObject();
        token = token.replace(prefix + " ", ""); 
        Claims claims = Jwts.parser()
                .setSigningKey(key)
                .parseClaimsJws(token)
                .getBody();
        object.setSubject(claims.getSubject());
        object.setExpiration(claims.getExpiration());
        object.setIssuedAt(claims.getIssuedAt());

        @SuppressWarnings("unchecked")
		List<String> roleNames = (List<String>) claims.get(ROLES_AUTHORITIES);
        List<Roles> roles = roleNames.stream()
                .map(roleName -> {
                    Roles rol = new Roles();
                    rol.setNombre(roleName);
                    return rol;
                })
                .collect(Collectors.toList());
        object.setRoles(roles);

        return object;
    }
    
}