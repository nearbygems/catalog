package kz.nearbygems.catalog.model.web;

import io.swagger.v3.oas.annotations.media.Schema;
import kz.nearbygems.catalog.model.elastic.TagDocument;
import lombok.experimental.FieldNameConstants;

@FieldNameConstants
@Schema(description = "Тэг")
public record TagRecord(@Schema(description = "Идентификатор", example = "MCp1y5Is3ym5E5B3")
                        String id,
                        @Schema(description = "Наименование", example = "Mobile_AD")
                        String name) {

  public TagDocument toDocument() {
    return new TagDocument(name);
  }

}
