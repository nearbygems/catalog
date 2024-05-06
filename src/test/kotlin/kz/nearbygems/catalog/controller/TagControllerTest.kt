package kz.nearbygems.catalog.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import kz.nearbygems.catalog.model.web.TagRecord
import kz.nearbygems.catalog.service.TagService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import kotlin.test.Test

@WebMvcTest(TagController::class)
class TagControllerTest {

  @Autowired
  lateinit var mvc: MockMvc

  @MockkBean
  lateinit var service: TagService

  @Test
  fun `should return list of tags`() {

    val firstRecord = TagRecord("1", "first")
    val secondRecord = TagRecord("2", "second")
    val thirdRecord = TagRecord("3", "third")

    val from = 0
    val size = 3
    val order = "Desc"

    val tags = listOf(firstRecord, secondRecord, thirdRecord)

    every { service.findAll(from, size, order) } returns tags

    //
    mvc.get("/api/v3/tags") {
      param("from", from.toString())
      param("size", size.toString())
      param("order", order)
    }.andExpectAll {
      status().isOk
      content { contentType(MediaType.APPLICATION_JSON) }
      jsonPath("$.length()") { value(tags.size) }
      jsonPath("$..id") {
        arrayOf(firstRecord.id, secondRecord.id, thirdRecord.id)
      }
      jsonPath("$..name") {
        arrayOf(firstRecord.name, secondRecord.name, thirdRecord.name)
      }
    }
    //

    verify(exactly = 1) {
      service.findAll(from, size, order)
    }

  }

  @Test
  fun `should return a tag when it is saved`() {

    val tagId = "tagId"

    val record = TagRecord(tagId, "name")

    every { service.findById(tagId) } returns record

    //
    mvc.get("/api/v3/tags/$tagId").andExpectAll {
      status().isOk
      content { contentType(MediaType.APPLICATION_JSON) }
      jsonPath("$.id") { value(record.id) }
      jsonPath("$.name") { value(record.name) }
    }
    //

    verify(exactly = 1) {
      service.findById(tagId)
    }

  }

}