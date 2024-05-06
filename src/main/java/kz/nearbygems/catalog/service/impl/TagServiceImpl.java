package kz.nearbygems.catalog.service.impl;

import kz.nearbygems.catalog.controller.exceptions.ElasticException;
import kz.nearbygems.catalog.controller.exceptions.NotFoundException;
import kz.nearbygems.catalog.model.web.TagRecord;
import kz.nearbygems.catalog.repository.TagRepository;
import kz.nearbygems.catalog.service.TagService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
record TagServiceImpl(TagRepository repository) implements TagService {

  @Override
  public TagRecord findById(String id) throws NotFoundException, ElasticException {
    return repository.findById(id)
                     .toRecord(id);
  }

  @Override
  public List<TagRecord> findAll(int from, int size, String order) throws ElasticException {
    return repository.findAll(from, size, order).stream()
                     .map(pair -> pair.value().toRecord(pair.key()))
                     .toList();
  }

  @Override
  public String createTag(TagRecord newRecord) throws ElasticException {
    return repository.createTag(newRecord.toDocument());
  }

  @Override
  public void updateTag(String id, TagRecord record) throws ElasticException {
    repository.updateTag(id, record.toDocument());
  }

  @Override
  public void deleteTag(String id) throws ElasticException {
    repository.deleteTag(id);
  }

}
