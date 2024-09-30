package org.campus.connect.message.firebase;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api")
@Tag(name = "Firebase", description = "Enviar notificações")
public class FirebaseResource {
  private final FirebaseService service;

  public FirebaseResource(final FirebaseService firebaseService) {
    this.service = firebaseService;
  }

  @PostMapping("/public/send")
  public String send(@RequestBody final FirebaseMessageDTO message) {
    return service.sendNotification(message);
  }
}
