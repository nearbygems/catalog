package kz.nearbygems.catalog.repository.impl

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch._types.query_dsl.Query
import kz.nearbygems.catalog.model.enums.Index
import org.assertj.core.api.Assertions.assertThat
import org.elasticsearch.client.ResponseException
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.GenericContainer
import kotlin.test.Test

@SpringBootTest
@ContextConfiguration(initializers = [ElasticContainer.Initializer::class])
class ElasticContainer {

  @Autowired
  protected lateinit var client: ElasticsearchClient

  companion object {
    private val elastic = GenericContainer<Nothing>("elasticsearch:8.12.1")
        .apply {
          withEnv("discovery.type", "single-node")
          withEnv("xpack.security.enabled", "false")
          withCreateContainerCmdModifier { it.hostConfig!!.withMemory(1024L * 1024 * 1024) }
          withExposedPorts(9200)
        }
  }

  internal class Initializer : ApplicationContextInitializer<ConfigurableApplicationContext> {
    override fun initialize(configurableApplicationContext: ConfigurableApplicationContext) {
      elastic.start()
      System.setProperty("elastic.host", elastic.host)
      System.setProperty("elastic.port", "${elastic.getMappedPort(9200)}")
      System.setProperty("elastic.max-cons", "100")
    }
  }

  @Test
  fun `just check that elastic container is running`() {
    assertThat(elastic.isRunning).isTrue
  }

  @BeforeEach
  fun clean() {
    Index.entries.forEach { index ->
      try {
        client.deleteByQuery { d ->
          d.index(index.code)
              .query(Query.of { f -> f.matchAll { it } })
              .refresh(true)
        }
      } catch (_: ResponseException) {
      }
    }

  }

}