package org.campus.connect.message;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
  info = @Info(
    title = "Campus Connect",
    version = "0.1 - Alpha",
    description = "Aplicação de envio de informações para usuários - API"
  )
)
@SecurityScheme(
  name = "bearerAuth",
  description = "JWT auth",
  scheme = "bearer",
  type = SecuritySchemeType.HTTP,
  bearerFormat = "JWT",
  in = SecuritySchemeIn.HEADER
)
public class MessageApplication {

  public static void main(String[] args) {
    SpringApplication.run(MessageApplication.class, args);
  }

}
