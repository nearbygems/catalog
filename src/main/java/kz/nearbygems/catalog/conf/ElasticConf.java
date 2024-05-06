package kz.nearbygems.catalog.conf;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticConf {

  @Bean
  public RestClient restClient(@Value("${elastic.host}") String host,
                               @Value("${elastic.port}") int port) {

    return RestClient.builder(new HttpHost(host, port))
                     .setRequestConfigCallback(call -> call.setConnectTimeout(Integer.MAX_VALUE)
                                                           .setSocketTimeout(Integer.MAX_VALUE))
                     .build();
  }

  @Bean
  public ElasticsearchClient client(RestClient restClient) {

    final var transport = new RestClientTransport(restClient, new JacksonJsonpMapper());

    return new ElasticsearchClient(transport);
  }

}
