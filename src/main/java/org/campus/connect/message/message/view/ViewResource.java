package org.campus.connect.message.message.view;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.campus.connect.message.utils.GenericResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
@Tag(name = "Links", description = "Gerenciamento de links")
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
}
