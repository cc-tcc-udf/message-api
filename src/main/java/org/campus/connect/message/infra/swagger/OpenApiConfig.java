package org.campus.connect.message.infra.swagger;

import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
  @Bean
  public SwaggerIndexTransformer swaggerIndexTransformer(
    SwaggerUiConfigProperties a,
    SwaggerUiOAuthProperties b,
    SwaggerUiConfigParameters c,
    SwaggerWelcomeCommon d,
    ObjectMapperProvider e) {
    return new SwaggerCodeBlockTransformer(a, b, c, d, e);
  }
}
