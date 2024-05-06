package kz.nearbygems.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.nearbygems.catalog.controller.exceptions.ElasticException;
import kz.nearbygems.catalog.controller.exceptions.NotFoundException;
import kz.nearbygems.catalog.model.web.TagRecord;
import kz.nearbygems.catalog.service.TagService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/tags")
@Tag(name = "Тэги", description = "Различные тэги (Mobile_AD, CVM, Chat_Bot...)")
record TagController(TagService service) {

  @GetMapping
  @Operation(
      summary = "Список тэгов",
      description = "Возвращает список тэгов",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Список тэгов",
              content = @Content(mediaType = "application/json",
                                 schema = @Schema(implementation = TagRecord.class))
          )
      }
  )
  private List<TagRecord> findAll(@RequestParam("from") int from,
                                  @RequestParam("size") int size,
                                  @RequestParam("order") String order) throws ElasticException {
    return service.findAll(from, size, order);
  }

  @GetMapping("/{id}")
  @Operation(
      summary = "Тэг",
      description = "Возвращает тэг по идентификатору",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Тэг",
              content = @Content(mediaType = "application/json",
                                 schema = @Schema(implementation = TagRecord.class))
          )
      }
  )
  private TagRecord findById(@PathVariable String id) throws NotFoundException, ElasticException {
    return service.findById(id);
  }

  @PostMapping
  @Operation(
      summary = "Создание тэга",
      description = "Создает тэг",
      responses = {
          @ApiResponse(
              responseCode = "200",
              description = "Идентификатор тэга",
              content = @Content(mediaType = "application/json",
                                 schema = @Schema(implementation = String.class))
          )
      }
  )
  private ResponseEntity<Void> createTag(@RequestBody TagRecord newRecord,
                                         UriComponentsBuilder ucb) throws ElasticException {

    final var id = service.createTag(newRecord);

    final var uri = ucb.path("tags/{id}")
                       .buildAndExpand(id)
                       .toUri();

    return ResponseEntity.created(uri).build();
  }

  @PutMapping("/{id}")
  @Operation(
      summary = "Обновляет тэг",
      description = "Обновляет тэг по идентификатору",
      responses = {
          @ApiResponse(
              responseCode = "200"
          )
      }
  )
  private void updateTag(@PathVariable String id,
                         @RequestBody TagRecord record) throws ElasticException {
    service.updateTag(id, record);
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Удаляет тэг",
      description = "Удаляет тэг по идентификатору",
      responses = {
          @ApiResponse(
              responseCode = "200"
          )
      }
  )
  private void deleteTag(@PathVariable String id) throws ElasticException {
    service.deleteTag(id);
  }

}
