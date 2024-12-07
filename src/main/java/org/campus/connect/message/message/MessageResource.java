package org.campus.connect.message.message;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.campus.connect.message.message.mobile.InfosDTO;
import org.campus.connect.message.utils.GenericResource;
import org.campus.connect.message.utils.dtos.ReturnObjDTO;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

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
  public ReturnObjDTO listByResp(@Parameter(description = "ID do professor", required = true)
                                 @PathVariable final UUID id) {
    try {
      return new ReturnObjDTO(service.findAllResp(id), true);
    } catch (Exception e) {
      return new ReturnObjDTO(e, false);
    }
  }

  @GetMapping(value = "/private/msg/mobile/list/{alunoId}")
  @Operation(summary = "Listar mensagens", description = "Lista mensagens baseadas no flag e no aluno")
  @Tag(name = "Mobile")
  public ReturnObjDTO listMobile(
    @Parameter(description = "ID do aluno", required = true) @PathVariable final UUID alunoId,
    @Parameter(description = "Flag de filtro ('all', 'reads', 'not_read', 'favorites')", required = true)
    @RequestParam final String flag) {
    try {
      List<MessageDTO> messages = service.findMessagesByFlagAndStudent(alunoId, flag);
      return new ReturnObjDTO(messages, true);
    } catch (IllegalArgumentException e) {
      return new ReturnObjDTO(e.getMessage(), false);
    } catch (Exception e) {
      return new ReturnObjDTO(e, false);
    }
  }


  @GetMapping(value = "/private/msg/list/course/{id}")
  @Operation(summary = "Listar msg", description = "Lista todas as msg")
  public ReturnObjDTO listByCourse(@Parameter(description = "ID do curso", required = true)
                                   @PathVariable final UUID id) {
    try {
      return new ReturnObjDTO(service.findByIdCourse(id), true);
    } catch (Exception e) {
      return new ReturnObjDTO(e, false);
    }
  }

  @GetMapping(value = "/private/msg/list/resp/{id}")
  @Operation(summary = "Listar msg", description = "Lista as ultimas 5 mensagens")
  public ReturnObjDTO listByRespId(@Parameter(description = "ID do responsive", required = true)
                                   @PathVariable final UUID id) {
    try {
      return new ReturnObjDTO(service.findByListIdResp(id), true);
    } catch (Exception e) {
      return new ReturnObjDTO(e, false);
    }
  }

  @GetMapping(value = "/private/msg/mobile/qtds/{id}")
  @Operation(summary = "Informação contendo a qtd de items", description = "Informação contendo a qtd de items de cada categoria")
  @Tag(name = "Mobile")
  public ReturnObjDTO getQtds(@Parameter(description = "ID do aluno", required = true)
                              @PathVariable final UUID id) {
    try {
      InfosDTO infos = service.findInfosAluno(id);
      return new ReturnObjDTO(infos, infos != null);
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

  @GetMapping(value = "/private/msg/mobile/get/{idMsg}")
  @Operation(summary = "Buscar mensagem por ID", description = "Recupera detalhes da mensagem e seu status de visualização e favorito para um usuário")
  @Tag(name = "Mobile")
  public ReturnObjDTO getById(@Parameter(description = "ID da message a ser retornada", required = true)
                              @PathVariable final UUID idMsg,
                              @Parameter(description = "ID do aluno", required = true)
                              @RequestParam UUID alunoId) {
    try {
      return new ReturnObjDTO(service.findMsgByIdMobile(idMsg, alunoId), true);
    } catch (Exception e) {
      return new ReturnObjDTO(e, false);
    }
  }


}
