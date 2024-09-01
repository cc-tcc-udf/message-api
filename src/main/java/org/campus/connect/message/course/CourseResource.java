package org.campus.connect.message.course;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.campus.connect.message.responseReturn.ReturnObjDTO;
import org.campus.connect.message.utils.GenericResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("api")
@Tag(name = "Course", description = "Gerenciamento de Mensagens")
public class CourseResource extends GenericResource<CourseDTO, CourseResource> {
  private final CourseService service;
  private final CourseMapper mapper;

  public CourseResource(CourseService service, CourseMapper mapper) {
    super(service, "api/");
    this.service = service;
    this.mapper = mapper;
  }

  @GetMapping(value = "/public/course/list")
  @Operation(
    summary = "Listar cursos",
    description = "Lista todos os cursos com base no parâmetro 'isGroup'. Se 'isGroup' for verdadeiro, lista apenas os cursos que são grupos. Se for falso, lista cursos que não são grupos.",
    tags = {"Cursos"}
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Cursos listados com sucesso",
      content = @Content(
        schema = @Schema(implementation = ReturnObjDTO.class),
        examples = @ExampleObject(value = "{ \"success\": true, \"message\": \"Requisição realizada com sucesso\", \"dataList\": [[{ \"name\": \"Curso A\", \"description\": \"Descrição A\", \"abbreviation\": \"CA\", \"isGroup\": false, \"courseGroupId\": null }]] }")
      )
    ),
    @ApiResponse(responseCode = "400", description = "Erro na requisição",
      content = @Content(
        schema = @Schema(implementation = ReturnObjDTO.class),
        examples = @ExampleObject(value = "{ \"success\": false, \"message\": \"Erro ao realizar requisição: <detalhes do erro>\" }")
      )
    )
  })
  public ReturnObjDTO list(@Parameter(
    name = "isGroup",
    description = "Filtro para determinar se deve listar apenas cursos que são grupos (true) ou cursos que não são grupos (false).",
    required = true,
    example = "false"
  ) @RequestParam(value = "isGroup") Boolean isGroup) {
    ReturnObjDTO dto = new ReturnObjDTO();
    try {
      List<CourseDTO> list = this.service.findAll(isGroup);
      dto.setMessage("Requisição realizada com sucesso");
      dto.setSuccess(true);
      dto.setDataList(Collections.singletonList(list));
    } catch (Exception e) {
      dto.setSuccess(false);
      dto.setMessage("Erro ao realizar requisição: " + e.getMessage());
    }
    return dto;
  }

  @PostMapping(value = "/public/course/create")
  @Operation(
    summary = "Criar curso",
    description = "Cria um novo curso. Se o curso for um grupo, não deve ter um grupo pai.",
    tags = {"Cursos"}
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Curso criado com sucesso",
      content = @Content(schema = @Schema(implementation = ReturnObjDTO.class))),
    @ApiResponse(responseCode = "400", description = "Erro na criação do curso",
      content = @Content(schema = @Schema(implementation = ReturnObjDTO.class)))
  })
  public ResponseEntity<ReturnObjDTO> create(
    @Parameter(description = "DTO contendo as informações do curso a ser criado", required = true)
    @RequestBody CourseDTO dto) {
    ReturnObjDTO dtoReturn = new ReturnObjDTO();
    try {
      CourseDTO createdCourse = this.service.create(dto);
      dtoReturn.setMessage("Curso criado com sucesso");
      dtoReturn.setSuccess(true);
      dtoReturn.setData(createdCourse);
    } catch (Exception e) {
      dtoReturn.setSuccess(false);
      dtoReturn.setMessage("Erro ao criar curso: " + e.getMessage());
    }
    return ResponseEntity.ok(dtoReturn);
  }
}
