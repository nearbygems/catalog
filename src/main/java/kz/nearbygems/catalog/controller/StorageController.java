package kz.nearbygems.catalog.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.nearbygems.catalog.controller.exceptions.ElasticException;
import kz.nearbygems.catalog.controller.exceptions.NotFoundException;
import kz.nearbygems.catalog.model.web.StorageRecord;
import kz.nearbygems.catalog.service.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/storage")
@Tag(name = "Хранилища", description = "Различные хранилища (kafka, postgresql, hdfs, greenplum)")
record StorageController(StorageService service) {

  @GetMapping
  @Operation(summary = "Список хранилищ",
             description = "Возвращает список хранилищ",
             responses = {@ApiResponse(responseCode = "200",
                                       description = "Список хранилищ",
                                       content = @Content(mediaType = "application/json",
                                                          schema = @Schema(implementation = StorageRecord.class)))})
  private List<StorageRecord> findAll(@RequestParam("from") int from,
                                      @RequestParam("size") int size,
                                      @RequestParam("order") String order) throws ElasticException {
    return service.findAll(from, size, order);
  }

  @GetMapping("/{id}")
  @Operation(summary = "Хранилище",
             description = "Возвращает хранилище по идентификатору",
             responses = {@ApiResponse(responseCode = "200",
                                       description = "Хранилище",
                                       content = @Content(mediaType = "application/json",
                                                          schema = @Schema(implementation = StorageRecord.class)))})
  private StorageRecord findBydId(@PathVariable String id) throws NotFoundException, ElasticException {
    return service.findById(id);
  }

  @PostMapping
  @Operation(summary = "Сохранение хранилища",
             description = "Сохраняет хранилище",
             responses = {@ApiResponse(responseCode = "200",
                                       description = "Идентификатор хранилища",
                                       content = @Content(mediaType = "application/json",
                                                          schema = @Schema(implementation = String.class)))})
  private ResponseEntity<Void> createTag(@RequestBody StorageRecord newRecord,
                                         UriComponentsBuilder ucb) throws ElasticException {

    final var id = service.createStorage(newRecord);

    final var uri = ucb.path("storage/{id}")
                       .buildAndExpand(id)
                       .toUri();

    return ResponseEntity.created(uri).build();
  }

  @PutMapping("/{id}")
  @Operation(summary = "Сохранение хранилища",
             description = "Обновляет хранилище",
             responses = {@ApiResponse(responseCode = "200",
                                       description = "Идентификатор хранилища",
                                       content = @Content(mediaType = "application/json",
                                                          schema = @Schema(implementation = String.class)))})
  private void updateStorage(@PathVariable String id,
                             @RequestBody StorageRecord record) throws ElasticException {
    service.updateStorage(id, record);
  }

  @DeleteMapping("/{id}")
  @Operation(
      summary = "Удаляет хранилище",
      description = "Удаляет хранилизще по идентификатору",
      responses = {
          @ApiResponse(
              responseCode = "200"
          )
      }
  )
  private void deleteStorage(@PathVariable String id) throws ElasticException {
    service.deleteStorage(id);
  }

}
