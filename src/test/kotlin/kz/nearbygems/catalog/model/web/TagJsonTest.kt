package kz.nearbygems.catalog.model.web

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester
import kotlin.test.Test

@JsonTest
class TagJsonTest {

  @Autowired
  private lateinit var json: JacksonTester<TagRecord>

  @Autowired
  private lateinit var jsonList: JacksonTester<Array<TagRecord>>

  private lateinit var tags: Array<TagRecord>

  @BeforeEach
  fun setUp() {
    tags = arrayOf(TagRecord("1", "first"),
                   TagRecord("2", "second"),
                   TagRecord("3", "third"))
  }

  @Test
  fun `tag serialization`() {
    val tag = tags[0]
    assertThat(json.write(tag)).isStrictlyEqualToJson("tag.json")
    assertThat(json.write(tag)).hasJsonPathStringValue("@.id")
    assertThat(json.write(tag)).extractingJsonPathStringValue("@.id").isEqualTo("1")
    assertThat(json.write(tag)).hasJsonPathStringValue("@.name")
    assertThat(json.write(tag)).extractingJsonPathStringValue("@.name").isEqualTo("first")
  }

  @Test
  fun `tag deserialization`() {
    val expected = """
      {
      "id": "1",
      "name": "first"
      }
      """
    assertThat(json.parse(expected)).isEqualTo(TagRecord("1", "first"))
    assertThat(json.parseObject(expected).id).isEqualTo("1")
    assertThat(json.parseObject(expected).name).isEqualTo("first")
  }

  @Test
  fun `tag list serialization`() {
    assertThat(jsonList.write(tags)).isStrictlyEqualToJson("tags.json")
  }

  @Test
  fun `tag list deserialization`() {
    val expected = """
    [
    { "id": "1", "name": "first" },
    { "id": "2", "name": "second"},
    { "id": "3", "name": "third" }
    ]
    """
    assertThat(jsonList.parse(expected)).isEqualTo(tags)
  }

}