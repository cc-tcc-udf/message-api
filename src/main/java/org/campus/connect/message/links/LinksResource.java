package org.campus.connect.message.links;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.campus.connect.message.message.MessageDTO;
import org.campus.connect.message.utils.GenericResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api")
@Tag(name = "Links", description = "Gerenciamento de links")
public class LinksResource extends GenericResource<LinksDTO, LinksResource> {
  private final LinksService service;
  private final LinksMapper mapper;

  public LinksResource(LinksService service, LinksMapper mapper) {
    super(service, "api/");
    this.service = service;
    this.mapper = mapper;
  }

  @GetMapping(value = "/public/links/list")
  @Operation(summary = "Listar msg", description = "Lista todas as msg")
  public List<LinksDTO> listUsers() {
    return service.findAll();
  }
}
