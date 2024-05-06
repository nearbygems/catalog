package kz.nearbygems.catalog.repository;

import kz.nearbygems.catalog.controller.exceptions.ElasticException;
import kz.nearbygems.catalog.controller.exceptions.NotFoundException;
import kz.nearbygems.catalog.model.elastic.StorageDocument;

import java.util.Map;

public interface StorageRepository {

  StorageDocument findById(String id) throws NotFoundException, ElasticException;

  Map<String, StorageDocument> findAll(int from, int size, String order) throws ElasticException;

  String createStorage(StorageDocument document) throws ElasticException;

  void updateStorage(String id, StorageDocument document) throws ElasticException;

  void deleteStorage(String id) throws ElasticException;

}