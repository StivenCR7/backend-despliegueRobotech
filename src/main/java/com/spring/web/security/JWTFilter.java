package com.spring.web.security;


import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import com.spring.web.model.Roles;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JWTFilter extends OncePerRequestFilter {

	 private List<SimpleGrantedAuthority> authorities(List<Roles> roles) {
	        return roles.stream()
	                    .map(rol -> new SimpleGrantedAuthority("ROLE_" + rol.getNombre().replace("ROLE_", "")))
	                    .collect(Collectors.toList());
	    }

	    @Override
	    protected void doFilterInternal(HttpServletRequest request,
	                                    HttpServletResponse response,
	                                    FilterChain filterChain) throws ServletException, IOException {

	        String token = request.getHeader(JWTCreator.HEADER_AUTHORIZATION);

	        try {
	            if (token != null && !token.isEmpty() && token.startsWith(SecurityConfig.PREFIX)) {
	                JWTObject tokenObject = JWTCreator.create(token, SecurityConfig.PREFIX, SecurityConfig.KEY);

	                List<SimpleGrantedAuthority> authorities = authorities(tokenObject.getRoles());

	                UsernamePasswordAuthenticationToken userToken =
	                        new UsernamePasswordAuthenticationToken(
	                                tokenObject.getSubject(),
	                                null,
	                                authorities);

	                SecurityContextHolder.getContext().setAuthentication(userToken);
	            } else {
	                SecurityContextHolder.clearContext();
	            }
	            filterChain.doFilter(request, response);
	        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException e) {
	            e.printStackTrace();
	            response.setStatus(HttpStatus.FORBIDDEN.value());
	            response.getWriter().write("JWT token Invalido");}
	    }
	}	


	       
	    

