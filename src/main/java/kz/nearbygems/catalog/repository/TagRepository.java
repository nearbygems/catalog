package kz.nearbygems.catalog.repository;

import co.elastic.clients.util.Pair;
import kz.nearbygems.catalog.controller.exceptions.ElasticException;
import kz.nearbygems.catalog.controller.exceptions.NotFoundException;
import kz.nearbygems.catalog.model.elastic.TagDocument;

import java.util.List;

public interface TagRepository {

  TagDocument findById(String id) throws NotFoundException, ElasticException;

  List<Pair<String, TagDocument>> findAll(int from, int size, String order) throws ElasticException;

  String createTag(TagDocument document) throws ElasticException;

  void updateTag(String id, TagDocument document) throws ElasticException;

  void deleteTag(String id) throws ElasticException;

}
