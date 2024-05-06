package kz.nearbygems.catalog.service;

import kz.nearbygems.catalog.controller.exceptions.ElasticException;
import kz.nearbygems.catalog.controller.exceptions.NotFoundException;
import kz.nearbygems.catalog.model.web.TagRecord;

import java.util.List;

public interface TagService {

  TagRecord findById(String id) throws NotFoundException, ElasticException;

  List<TagRecord> findAll(int from, int size, String order) throws ElasticException;

  String createTag(TagRecord newRecord) throws ElasticException;

  void updateTag(String id, TagRecord record) throws ElasticException;

  void deleteTag(String id) throws ElasticException;

}
