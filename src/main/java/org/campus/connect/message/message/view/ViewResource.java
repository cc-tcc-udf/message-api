package org.campus.connect.message.message.view;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.campus.connect.message.utils.GenericResource;
import org.campus.connect.message.utils.dtos.ReturnObjDTO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api")
@Tag(name = "Messages views", description = "Visualização das mensagens")
public class ViewResource extends GenericResource<ViewDTO, ViewResource> {
  private final ViewService service;
  private final ViewMapper mapper;

  public ViewResource(ViewService service, ViewMapper mapper) {
    super(service, "api/");
    this.service = service;
    this.mapper = mapper;
  }

  @GetMapping(value = "/public/view/list")
  @Operation(summary = "Listar msg", description = "Lista todas as msg")
  public List<ViewDTO> listUsers() {
    return service.findAll();
  }

  @GetMapping(value = "/private/view/mobile/setView")
  @Tag(name = "Mobile")
  @Operation(summary = "Listar msg", description = "Lista todas as msg")
  public ReturnObjDTO setViews(@RequestParam UUID idUser,
                               @RequestParam UUID idMessage) {
    try {
      return new ReturnObjDTO(service.setView(idUser, idMessage), true);
    } catch (Exception e) {
      return new ReturnObjDTO(e, false);
    }
  }

  @GetMapping(value = "/private/view/list/{id}")
  @Tag(name = "Mobile")
  @Operation(summary = "Listar msg", description = "Lista todas as msg")
  public ReturnObjDTO getByIdMsg(@PathVariable UUID id) {
    try {
      return new ReturnObjDTO(service.getByIdMsg(id), true);
    } catch (Exception e) {
      return new ReturnObjDTO(e, false);
    }
  }

}
