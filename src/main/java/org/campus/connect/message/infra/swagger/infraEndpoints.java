package org.campus.connect.message.infra.swagger;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@RequestMapping("")
public class infraEndpoints {
  @Hidden
  @GetMapping("/static/dark-swagger-ui.css")
  public ResponseEntity<Resource> getCss() {
    Resource cssResource = new ClassPathResource("static/dark-swagger-ui.css");
    try {
      if (cssResource.exists()) {
        return ResponseEntity.ok()
          .contentType(MediaType.valueOf("text/css"))
          .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"dark-swagger-ui.css\"")
          .body(cssResource);
      } else {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
      }
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
  }

  @GetMapping("")
  @Hidden
  public RedirectView redirectToSwagger() {
    return new RedirectView("/swagger-ui/index.html");
  }
}
