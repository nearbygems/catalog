package kz.nearbygems.catalog.service.impl;

import kz.nearbygems.catalog.controller.exceptions.ElasticException;
import kz.nearbygems.catalog.controller.exceptions.NotFoundException;
import kz.nearbygems.catalog.model.web.StorageRecord;
import kz.nearbygems.catalog.repository.StorageRepository;
import kz.nearbygems.catalog.service.StorageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
record StorageServiceImpl(StorageRepository repository) implements StorageService {

  @Override
  public StorageRecord findById(String id) throws NotFoundException, ElasticException {
    return repository.findById(id).toRecord(id);
  }

  @Override
  public List<StorageRecord> findAll(int from, int size, String order) throws ElasticException {
    return repository.findAll(from, size, order).entrySet().stream()
                     .map(e -> e.getValue().toRecord(e.getKey()))
                     .toList();
  }

  @Override
  public String createStorage(StorageRecord newRecord) throws ElasticException {
    return repository.createStorage(newRecord.toDocument());
  }

  @Override
  public void updateStorage(String id, StorageRecord record) throws ElasticException {
    repository.updateStorage(id, record.toDocument());
  }

  @Override
  public void deleteStorage(String id) throws ElasticException {
    repository.deleteStorage(id);
  }

}
