package kz.nearbygems.catalog.service;

import kz.nearbygems.catalog.controller.exceptions.ElasticException;
import kz.nearbygems.catalog.controller.exceptions.NotFoundException;
import kz.nearbygems.catalog.model.web.StorageRecord;

import java.util.List;

public interface StorageService {

  StorageRecord findById(String id) throws NotFoundException, ElasticException;

  List<StorageRecord> findAll(int from, int size, String order) throws ElasticException;

  String createStorage(StorageRecord newRecord) throws ElasticException;

  void updateStorage(String id, StorageRecord record) throws ElasticException;

  void deleteStorage(String id) throws ElasticException;

}