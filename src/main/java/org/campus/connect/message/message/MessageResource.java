package org.campus.connect.message.message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import org.campus.connect.message.mail.MailDTO;
import org.campus.connect.message.mail.MailService;
import org.campus.connect.message.utils.GenericResource;
import org.campus.connect.message.utils.dtos.ReturnObjDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api")
@Tag(name = "Message", description = "Gerenciamento de Mensagens")
public class MessageResource extends GenericResource<MessageDTO, MessageResource> {
  private final MessageService service;
  private final MessageMapper mapper;
  private final MailService mailService;

  public MessageResource(MessageService service, MessageMapper mapper, final MailService mailService) {
    super(service, "api/");
    this.service = service;
    this.mapper = mapper;
    this.mailService = mailService;
  }

  @GetMapping(value = "/private/msg/{id}")
  @Operation(summary = "Buscar msg pelo id", description = "Lista todas as msg")
  public ReturnObjDTO getById(@Parameter(description = "ID da message a ser retornada", required = true)
                              @PathVariable final UUID id) {
    try {
      return new ReturnObjDTO(service.findMsgById(id), true);
    } catch (Exception e) {
      return new ReturnObjDTO(e, false);
    }
  }

  @GetMapping(value = "/private/msg/listAll")
  @Operation(summary = "Listar msg", description = "Lista todas as msg")
  @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
  public ReturnObjDTO list() {
    try {
      return new ReturnObjDTO(service.findAll(), true);
    } catch (Exception e) {
      return new ReturnObjDTO(e, false);
    }
  }

  @GetMapping(value = "/private/msg/listByResp/{id}")
  @Operation(summary = "Listar msg", description = "Lista todas as msg")
  @PreAuthorize("hasAnyRole('ROLE_PROF')")
  public ReturnObjDTO listByResp(@Parameter(description = "ID do aluno", required = true)
                                 @PathVariable final UUID id) {
    try {
      return new ReturnObjDTO(service.findAllResp(id), true);
    } catch (Exception e) {
      return new ReturnObjDTO(e, false);
    }
  }

  @GetMapping(value = "/private/msg/mobile/list/{id}")
  @Operation(summary = "Listar msg", description = "Lista todas as msg")
  @Tag(name = "Mobile")
  public ReturnObjDTO listMobile(@Parameter(description = "ID do do curso", required = true)
                                 @PathVariable final UUID id) {
    try {
      return new ReturnObjDTO(service.findByIdCourse(id), true);
    } catch (Exception e) {
      return new ReturnObjDTO(e, false);
    }
  }

  @PostMapping(value = "/private/msg/create")
  @Operation(summary = "Criar mensagem", description = "criação de mensagem")
  public ReturnObjDTO createMessage(@RequestBody MessageDTO message) {
    try {
      MessageDTO msg = service.create(message);
      return new ReturnObjDTO(msg, true);
    } catch (Exception e) {
      return new ReturnObjDTO(e, false);
    }
  }

  @PostMapping(value = "/private/msg/send")
  @Operation(summary = "Criar mensagem", description = "criação de mensagem")
  public ReturnObjDTO sendMessage(@RequestBody MessageDTO message) {
    try {
      MessageDTO msg = service.send(message);
      if (msg != null)
        return new ReturnObjDTO(msg, true);
      else return new ReturnObjDTO(null, false);
    } catch (Exception e) {
      return new ReturnObjDTO(e, false);
    }
  }

  @PostMapping("email")
  public String enviarEmail(@RequestBody MailDTO dto) throws MessagingException {
    try {
      this.mailService.sendWelcomeEmail(dto);
    } catch (Exception e) {
      return "Erro ao enviar e-mail" + e.getMessage();
    }

    return "Email enviado com sucesso!";
  }
}
