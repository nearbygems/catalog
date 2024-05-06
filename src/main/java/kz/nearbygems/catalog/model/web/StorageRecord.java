package kz.nearbygems.catalog.model.web;

import co.elastic.clients.elasticsearch.core.GetResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import io.swagger.v3.oas.annotations.media.Schema;
import kz.nearbygems.catalog.model.elastic.StorageDocument;
import lombok.experimental.FieldNameConstants;

import java.util.Optional;

@FieldNameConstants
@Schema(description = "Хранилище")
public record StorageRecord(@Schema(description = "Идентификатор", example = "lIkRq9CcQtdQStmW")
                            String id,
                            @Schema(description = "Наименование", example = "postgresql")
                            String name) {

  public StorageDocument toDocument() {
    return new StorageDocument(name);
  }

  public static StorageRecord fromGetResponse(GetResponse<StorageDocument> getResponse) {
    return Optional.ofNullable(getResponse.source())
                   .map(document -> new StorageRecord(getResponse.id(), document.name()))
                   .orElse(null);
  }

  public static StorageRecord fromHit(Hit<StorageDocument> hit) {
    return Optional.ofNullable(hit.source())
                   .map(document -> new StorageRecord(hit.id(), document.name()))
                   .orElse(null);
  }

}
