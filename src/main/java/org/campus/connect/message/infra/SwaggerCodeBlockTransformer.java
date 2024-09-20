package org.campus.connect.message.infra;

import jakarta.servlet.http.HttpServletRequest;
import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springdoc.core.properties.SwaggerUiOAuthProperties;
import org.springdoc.core.providers.ObjectMapperProvider;
import org.springdoc.webmvc.ui.SwaggerIndexPageTransformer;
import org.springdoc.webmvc.ui.SwaggerWelcomeCommon;
import org.springframework.core.io.Resource;
import org.springframework.web.servlet.resource.ResourceTransformerChain;
import org.springframework.web.servlet.resource.TransformedResource;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.stream.Collectors;

public class SwaggerCodeBlockTransformer extends SwaggerIndexPageTransformer {


  public SwaggerCodeBlockTransformer(final SwaggerUiConfigProperties swaggerUiConfig,
                                     final SwaggerUiOAuthProperties swaggerUiOAuthProperties,
                                     final SwaggerUiConfigParameters swaggerUiConfigParameters,
                                     final SwaggerWelcomeCommon swaggerWelcomeCommon,
                                     final ObjectMapperProvider objectMapperProvider) {
    super(swaggerUiConfig, swaggerUiOAuthProperties, swaggerUiConfigParameters, swaggerWelcomeCommon, objectMapperProvider);
  }

  @Override
  public Resource transform(HttpServletRequest request,
                            Resource resource,
                            ResourceTransformerChain transformer) throws IOException {
    if (Objects.equals(resource.getFilename(), "index.html")) {
      try (InputStream is = resource.getInputStream();
           BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {

        String html = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        String transformedHtml = html
          .replace("</head>", "<link rel=\"stylesheet\" type=\"text/css\"" +
            " href=\"/static/dark-swagger-ui.css\" /></head>");
        return new TransformedResource(resource, transformedHtml.getBytes());
      }
    }

    return super.transform(request, resource, transformer);
  }
}
