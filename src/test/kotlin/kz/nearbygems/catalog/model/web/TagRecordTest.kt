package kz.nearbygems.catalog.model.web

import org.assertj.core.api.Assertions.assertThat
import kotlin.test.Test

class TagRecordTest {

  @Test
  fun `should return tag document from tag record`() {

    val record = TagRecord("some id", "some name")

    //
    val doc = record.toDocument()
    //

    assertThat(doc).isNotNull
    assertThat(doc.name).isEqualTo(record.name)
  }

}