package kz.nearbygems.catalog.model.elastic;

import kz.nearbygems.catalog.model.web.StorageRecord;

public record StorageDocument(String name) {

  public StorageRecord toRecord(String id) {
    return new StorageRecord(id, name);
  }

}