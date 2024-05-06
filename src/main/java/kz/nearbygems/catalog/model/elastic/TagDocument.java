package kz.nearbygems.catalog.model.elastic;

import kz.nearbygems.catalog.model.web.TagRecord;

public record TagDocument(String name) {

  public TagRecord toRecord(String id) {
    return new TagRecord(id, name);
  }

}
