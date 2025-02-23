
package com.spring.web.swagger;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

import org.springframework.context.annotation.Bean;

@Configuration
@OpenAPIDefinition
public class SwaggerConfig {

	@Bean
	OpenAPI api() {
	    return new OpenAPI()
	            .components(new Components()
	                    .addSecuritySchemes("bearerAuth",
	                            new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")))
	            .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
	            .info(new Info().title("API Rest Robotech").version("1.0")
	                    .contact(new Contact().name("Robotech"))
	                    .description("Documentación de la API para la gestión de torneos"));
	}
}