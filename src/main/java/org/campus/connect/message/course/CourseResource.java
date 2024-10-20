package org.campus.connect.message.course;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.campus.connect.message.course.dto.CourseCompleteDTO;
import org.campus.connect.message.course.dto.SubCourseDTO;
import org.campus.connect.message.utils.ReturnObjDTO;
import org.campus.connect.message.utils.GenericResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

  @GetMapping(value = "/public/course/{id}")
  @Operation(
    summary = "Retorna um curso existente pelo ID",
    description = "Este endpoint retorna os detalhes de um curso específico com base no seu identificador único (ID). Se o curso existir, os detalhes completos serão retornados, incluindo informações sobre o curso e seus subcursos, se aplicável.",
    tags = {"Course"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Curso encontrado com sucesso",
      content = @Content(
        schema = @Schema(implementation = SubCourseDTO.class),
        examples = @ExampleObject(
          value = "{ \"id\": 1, \"name\": \"Subcurso 1\", \"description\": \"Descrição Subcurso 1\", \"abbreviation\": \"SC1\", \"isGroup\": false }, "
        )
      )
    ),
    @ApiResponse(
      responseCode = "404",
      description = "Curso não encontrado",
      content = @Content(
        schema = @Schema(implementation = ReturnObjDTO.class),
        examples = @ExampleObject(
          value = "{ \"success\": false, \"message\": \"Curso não encontrado\" }"
        )
      )
    )
  })
  public ResponseEntity<ReturnObjDTO> getCourse(
    @Parameter(description = "ID do curso a ser retornado", required = true)
    @PathVariable final Long id
  ) {
    ReturnObjDTO returnObjDTO = new ReturnObjDTO();
    try {
      CourseCompleteDTO course = service.findCourseById(id);
      returnObjDTO.setData(course);
      returnObjDTO.setSuccess(true);
      returnObjDTO.setMessage("Requisição realizada com sucesso!");
    } catch (Exception e) {
      returnObjDTO.setSuccess(false);
      returnObjDTO.setMessage("Erro ao realizar requisição: " + e.getMessage());
    }

    return ResponseEntity.ok(returnObjDTO);
  }


  @GetMapping(value = "/public/course/groups")
  @Operation(
    summary = "Listar grupos de cursos",
    description = "Este endpoint retorna uma lista de grupos de cursos disponíveis.",
    tags = {"Course"}
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Lista de grupos recuperada com sucesso",
      content = @Content(
        schema = @Schema(implementation = ReturnObjDTO.class),
        examples = @ExampleObject(
          value = "{ " +
            "\"success\": true, " +
            "\"message\": \"Requisição realizada com sucesso!\", " +
            "\"data\": [" +
            "  { " +
            "    \"id\": 1,    \"name\": \"Grupo 1(G1)\",     \"courses\": [" +
            "{ \"id\": 3, \"name\": \"Curso 1 do Grupo 1(C1G1)\" }, { \"id\": 5, \"name\": \"Curso 2 do Grupo 1(C2G1)\" }" +
            "]}]}"
        )
      )
    ),
    @ApiResponse(responseCode = "400", description = "Erro na requisição",
      content = @Content(
        schema = @Schema(implementation = ReturnObjDTO.class),
        examples = @ExampleObject(
          value = "{ \"success\": false, \"message\": \"Erro ao realizar requisição: <detalhes do erro>\" }"
        )
      )
    )
  })
  public ResponseEntity<ReturnObjDTO> groups() {
    ReturnObjDTO returnObjDTO = new ReturnObjDTO();
    try {
      List<CourseDTO> list = service.findGroups();
      returnObjDTO.setData(list);
      returnObjDTO.setSuccess(true);
      returnObjDTO.setMessage("Requisição realizada com sucesso!");
    } catch (Exception e) {
      returnObjDTO.setSuccess(false);
      returnObjDTO.setMessage("Erro ao realizar requisição: " + e.getMessage());
    }

    return ResponseEntity.ok(returnObjDTO);
  }


  @GetMapping(value = "/public/course/list")
  @Operation(
    summary = "Listar cursos",
    description = "Lista todos os cursos com base no parâmetro 'isGroup'. Se 'isGroup' for verdadeiro," +
      " lista apenas os cursos que são grupos. Se for falso, lista cursos que não são grupos.",
    tags = {"Course"}
  )
  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Lista de cursos recuperada com sucesso",
      content = @Content(
        mediaType = "application/json",
        examples = @ExampleObject(
          value = "{ \"success\": true, \"message\": \"Requisição realizada com sucesso\", \"dataList\": [[{ " +
            "\"name\": \"Curso A\", \"description\": \"Descrição A\", \"abbreviation\": \"CA\", " +
            "\"isGroup\": true, \"courses\": [" +
            "{ \"name\": \"Subcurso 1\", \"description\": \"Descrição do Subcurso 1\", \"abbreviation\": \"SC1\", \"isGroup\": false, \"courseGroupId\": 1 }, " +
            "{ \"name\": \"Subcurso 2\", \"description\": \"Descrição do Subcurso 2\", \"abbreviation\": \"SC2\", \"isGroup\": false, \"courseGroupId\": 1 }" +
            "] }]] }"
        )
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
      List<CourseCompleteDTO> list = this.service.findAll(isGroup);
      return new ReturnObjDTO(list, true);
    } catch (Exception e) {
      return new ReturnObjDTO(e, false);
    }
  }

  @PostMapping(value = "/public/course/create")
  @Operation(
    summary = "Criar um Novo Curso",
    description = "Este endpoint permite a criação de um novo curso. A requisição deve incluir todos os detalhes necessários para a criação do curso," +
      " como nome, descrição, abreviação, responsável, e se o curso pertence a um grupo ou não. Se o curso estiver marcado como um grupo (i.e., `isGroup` é `true`)," +
      " ele não deve ter um grupo pai (`courseGroupId` deve ser `null`). Caso contrário, se o curso não for um grupo (`isGroup` é `false`)," +
      " ele deve ser associado a um grupo pai existente por meio do `courseGroupId`, porém um curso não obrigatoriamente precisa esta em grupo. Este endpoint retorna as informações do curso criado," +
      " incluindo um identificador único gerado para o curso.",
    tags = {"Course"}
  )

  @ApiResponses(value = {
    @ApiResponse(
      responseCode = "200",
      description = "Curso criado com sucesso",
      content = @Content(
        schema = @Schema(implementation = ReturnObjDTO.class),
        examples = @ExampleObject(
          value = "{ \"success\": true, \"message\": \"Curso criado com sucesso\", \"data\": { " +
            "\"id\": 3, " +
            "\"name\": \"Curso Novo\", " +
            "\"description\": \"Descrição do Curso Novo\", " +
            "\"abbreviation\": \"CN\", " +
            "\"resp\": 1, " +
            "\"courseGroupId\": null, " +
            "\"isGroup\": false " +
            "} }"
        )
      )
    ),
    @ApiResponse(
      responseCode = "400",
      description = "Erro na criação do curso",
      content = @Content(
        schema = @Schema(implementation = ReturnObjDTO.class),
        examples = @ExampleObject(
          value = "{ \"success\": false, \"message\": \"Erro ao criar curso: <detalhes do erro>\" }"
        )
      )
    )
  })
  public ResponseEntity<ReturnObjDTO> create(
    @Parameter(description = "Dados do curso a ser criado", required = true)
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

  @PutMapping(value = "/public/course/update")
  @Operation(
    summary = "Atualizar um Curso Existente",
    description = "Este endpoint permite a atualização dos detalhes de um curso existente." +
      " A requisição deve incluir o identificador do curso (`id`) e os novos detalhes que devem ser atualizados." +
      " Se o curso for um grupo (`isGroup` é `true`), ele não deve ter um grupo pai (`courseGroupId` deve ser `null`)." +
      " Caso contrário, se o curso não for um grupo (`isGroup` é `false`), ele deve estar associado a um grupo pai existente por meio do `courseGroupId`, " +
      "porém um curso não obrigatoriamente precisa esta em grupo. " +
      "O endpoint retorna as informações do curso atualizado.",
    tags = {"Course"}
  )
  @ApiResponses(value = {
    @ApiResponse(responseCode = "200", description = "Curso atualizado com sucesso",
      content = @Content(
        schema = @Schema(implementation = ReturnObjDTO.class),
        examples = @ExampleObject(
          value = "{ \"success\": true, \"message\": \"Curso atualizado com sucesso\", \"data\": { " +
            "\"id\": 3, " +
            "\"name\": \"Nome editado\", " +
            "\"description\": \"Descrição editado\", " +
            "\"abbreviation\": \"NE\", " +
            "\"resp\": 1, " +
            "\"courseGroupId\": 1, " +
            "\"isGroup\": false " +
            "} }"
        )
      )
    ),
    @ApiResponse(responseCode = "400", description = "Erro na atualização do curso",
      content = @Content(schema = @Schema(implementation = ReturnObjDTO.class)))
  })
  public ResponseEntity<ReturnObjDTO> update(
    @Parameter(description = "DTO contendo as informações do curso a ser atualizado", required = true)
    @RequestBody CourseDTO dto) {
    ReturnObjDTO dtoReturn = new ReturnObjDTO();
    try {
      CourseDTO updatedCourse = this.service.update(dto);
      dtoReturn.setMessage("Curso atualizado com sucesso");
      dtoReturn.setSuccess(true);
      dtoReturn.setData(updatedCourse);
    } catch (Exception e) {
      dtoReturn.setSuccess(false);
      dtoReturn.setMessage("Erro ao atualizar curso: " + e.getMessage());
    }
    return ResponseEntity.ok(dtoReturn);
  }

}
