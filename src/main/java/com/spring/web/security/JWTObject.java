package com.spring.web.security;

import java.util.Date;
import java.util.List;
import com.spring.web.model.Roles;



public class JWTObject {
    private String subject; //nombre de usuario
    private Date issuedAt; //data creado del token
    private Date expiration; // data de expiracion del token
    private List<Roles> roles; //perfiles de acceso
	
    
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public Date getIssuedAt() {
		return issuedAt;
	}
	public void setIssuedAt(Date issuedAt) {
		this.issuedAt = issuedAt;
	}
	public Date getExpiration() {
		return expiration;
	}
	public void setExpiration(Date expiration) {
		this.expiration = expiration;
	}
	public List<Roles> getRoles() {
		return roles;
	}
	public void setRoles(List<Roles> roles) {
		this.roles = roles;
	}
	
	
}