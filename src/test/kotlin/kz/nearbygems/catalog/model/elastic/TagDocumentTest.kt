package kz.nearbygems.catalog.model.elastic

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class TagDocumentTest {

  @Test
  fun `should return tag record from tag document`() {

    val id = "some id"
    val document = TagDocument("some name")

    //
    val record = document.toRecord(id)
    //

    assertThat(record).isNotNull
    assertThat(record.id).isEqualTo(id)
    assertThat(record.name).isEqualTo(document.name)
  }

}