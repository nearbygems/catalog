package kz.nearbygems.catalog.conf;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import jakarta.annotation.PostConstruct;
import kz.nearbygems.catalog.model.enums.Index;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.RestClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class IndexConf {

  private final ElasticsearchClient client;
  private final RestClient restClient;

  public void createTagIndex() throws IOException {

    final var tagsNotExists = !client.indices().exists(e -> e.index(Index.TAGS.getCode())).value();

    if (tagsNotExists) {

      var inputStream = new ClassPathResource(Index.TAGS.getCode() + ".json").getInputStream();
      var json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

      performRequest("PUT", "/" + Index.TAGS.getCode(), json);
    }

  }

  public void createStorageIndex() throws IOException {

    final var storagesNotExists = !client.indices().exists(e -> e.index(Index.STORAGE.getCode())).value();

    if (storagesNotExists) {

      var inputStream = new ClassPathResource(Index.STORAGE.getCode() + ".json").getInputStream();
      var json = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

      performRequest("PUT", "/" + Index.STORAGE.getCode(), json);
    }

  }

  private void performRequest(String type, String uri, String json) throws IOException {

    final var request = new Request(type, uri);

    if (json != null) {
      request.setJsonEntity(json);
    }

    restClient.performRequest(request);
  }

  @PostConstruct
  public void init() {
    try {
      createTagIndex();
      createStorageIndex();
      log.info("Created required elastic indexes");
    } catch (IOException e) {
      log.error(e.getMessage());
    }
  }

}
