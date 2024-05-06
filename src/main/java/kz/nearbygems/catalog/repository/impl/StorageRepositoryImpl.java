package kz.nearbygems.catalog.repository.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.get.GetResult;
import co.elastic.clients.elasticsearch.core.search.Hit;
import kz.nearbygems.catalog.controller.exceptions.ElasticException;
import kz.nearbygems.catalog.controller.exceptions.NotFoundException;
import kz.nearbygems.catalog.model.elastic.StorageDocument;
import kz.nearbygems.catalog.model.elastic.TagDocument;
import kz.nearbygems.catalog.model.enums.Index;
import kz.nearbygems.catalog.model.web.StorageRecord;
import kz.nearbygems.catalog.repository.StorageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
record StorageRepositoryImpl(ElasticsearchClient client) implements StorageRepository {

  @Override
  public StorageDocument findById(String id) throws NotFoundException, ElasticException {
    try {
      return Optional.ofNullable(client.get(get -> get.index(Index.STORAGE.getCode()).id(id), StorageDocument.class))
                     .map(GetResult::source)
                     .orElseThrow(() -> new NotFoundException("Couldn't find a storage with id = " + id));
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new ElasticException(e.getMessage());
    }
  }

  @Override
  public Map<String, StorageDocument> findAll(int from, int size, String order) throws ElasticException {
    try {
      return client.search(search -> search.index(Index.STORAGE.getCode())
                                           .sort(sort -> sort.field(field -> field.field(StorageRecord.Fields.name)
                                                                                  .order(SortOrder.valueOf(order))))
                                           .from(from)
                                           .size(size),
                           StorageDocument.class)
                   .hits().hits().stream()
                   .filter(hit -> hit.source() != null)
                   .collect(Collectors.toMap(Hit::id, Hit::source));
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new ElasticException(e.getMessage());
    }
  }

  @Override
  public String createStorage(StorageDocument document) throws ElasticException {
    try {
      return client.create(create -> create.index(Index.STORAGE.getCode())
                                           .id(UUID.randomUUID().toString())
                                           .document(document))
                   .id();
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new ElasticException(e.getMessage());
    }
  }

  @Override
  public void updateStorage(String id, StorageDocument document) throws ElasticException {
    try {
      client.update(update -> update.index(Index.TAGS.getCode())
                                    .id(id)
                                    .docAsUpsert(false)
                                    .doc(document),
                    TagDocument.class);
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new ElasticException(e.getMessage());
    }
  }

  @Override
  public void deleteStorage(String id) throws ElasticException {
    try {
      client.delete(delete -> delete.index(Index.STORAGE.getCode())
                                    .id(id));
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new ElasticException(e.getMessage());
    }
  }

}
