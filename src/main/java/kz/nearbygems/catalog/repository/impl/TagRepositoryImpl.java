package kz.nearbygems.catalog.repository.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch.core.get.GetResult;
import co.elastic.clients.util.Pair;
import kz.nearbygems.catalog.controller.exceptions.ElasticException;
import kz.nearbygems.catalog.controller.exceptions.NotFoundException;
import kz.nearbygems.catalog.model.elastic.TagDocument;
import kz.nearbygems.catalog.model.enums.Index;
import kz.nearbygems.catalog.model.web.TagRecord;
import kz.nearbygems.catalog.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
record TagRepositoryImpl(ElasticsearchClient client) implements TagRepository {

  @Override
  public TagDocument findById(String id) throws NotFoundException, ElasticException {
    try {
      return Optional.ofNullable(client.get(get -> get.index(Index.TAGS.getCode()).id(id), TagDocument.class))
                     .map(GetResult::source)
                     .orElseThrow(() -> new NotFoundException("Couldn't find a tag with id = " + id));
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new ElasticException(e.getMessage());
    }
  }

  @Override
  public List<Pair<String, TagDocument>> findAll(int from, int size, String order) throws ElasticException {
    try {
      return client.search(search -> search.index(Index.TAGS.getCode())
                                           .sort(sort -> sort.field(field -> field.field(TagRecord.Fields.name)
                                                                                  .order(SortOrder.valueOf(order))))
                                           .from(from)
                                           .size(size),
                           TagDocument.class)
                   .hits().hits().stream()
                   .map(hit -> Pair.of(hit.id(), hit.source()))
                   .toList();
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new ElasticException(e.getMessage());
    }
  }

  @Override
  public String createTag(TagDocument document) throws ElasticException {
    try {
      return client.create(create -> create.index(Index.TAGS.getCode())
                                           .id(UUID.randomUUID().toString())
                                           .document(document))
                   .id();
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new ElasticException(e.getMessage());
    }
  }

  @Override
  public void updateTag(String id, TagDocument document) throws ElasticException {
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
  public void deleteTag(String id) throws ElasticException {
    try {
      client.delete(delete -> delete.index(Index.TAGS.getCode())
                                    .id(id));
    } catch (IOException e) {
      log.error(e.getMessage());
      throw new ElasticException(e.getMessage());
    }
  }

}
