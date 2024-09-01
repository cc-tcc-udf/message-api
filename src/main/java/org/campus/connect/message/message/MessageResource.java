package org.campus.connect.message.message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.campus.connect.message.utils.GenericResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api")
@Tag(name = "Message", description = "Gerenciamento de Mensagens")
public class MessageResource extends GenericResource<MessageDTO, MessageResource> {
  private final MessageService service;
  private final MessageMapper mapper;

  public MessageResource(MessageService service, MessageMapper mapper) {
    super(service, "api/");
    this.service = service;
    this.mapper = mapper;
  }

  @GetMapping(value = "/public/msg/list")
  @Operation(summary = "Listar msg", description = "Lista todas as msg")
  public List<MessageDTO> list() {
    return service.findAll();
  }

  @PostMapping(value = "/public/msg/create")
  @Operation(summary = "Criar mensagem", description = "criação de mensagem")
  public ResponseEntity<MessageDTO> createMessage(@RequestBody MessageDTO message) throws Exception {
    return ResponseEntity.ok(service.create(message));

  }
}
