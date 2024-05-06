package kz.nearbygems.catalog.repository.impl

import co.elastic.clients.elasticsearch._types.Refresh
import kz.nearbygems.catalog.model.elastic.TagDocument
import kz.nearbygems.catalog.model.enums.Index
import kz.nearbygems.catalog.repository.TagRepository
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import java.util.*
import kotlin.test.Test

class TagRepositoryImplTest : ElasticContainer() {

  @Autowired
  lateinit var repository: TagRepository

  @Test
  fun `should return a tag when it is saved`() {

    val id = UUID.randomUUID().toString()
    val document = saveTag(id, "some name")

    //
    val saved = repository.findById(id)
    //

    assertThat(saved.name).isEqualTo(document.name)
  }

  @Test
  fun `should return list of tags in asc order`() {

    val firstDocumentId = UUID.randomUUID().toString()
    val firstDocument = saveTag(firstDocumentId, "first tag")
    val secondDocumentId = UUID.randomUUID().toString()
    val secondDocument = saveTag(secondDocumentId, "second tag")
    val thirdDocumentId = UUID.randomUUID().toString()
    val thirdDocument = saveTag(thirdDocumentId, "third tag")

    //
    val tags = repository.findAll(0, 3, "Asc")
    //

    assertThat(tags).isNotEmpty
    assertThat(tags).hasSize(3)

    assertThat(tags[0].key()).isEqualTo(firstDocumentId)

    val tagsByIds = tags.associateBy { it.key() }

    tagsByIds[firstDocumentId]?.let {
      assertThat(it.value().name).isEqualTo(firstDocument.name)
    }
    tagsByIds[secondDocumentId]?.let {
      assertThat(it.value().name).isEqualTo(secondDocument.name)
    }
    tagsByIds[thirdDocumentId]?.let {
      assertThat(it.value().name).isEqualTo(thirdDocument.name)
    }
  }

  @Test
  fun `should return list of tags in desc order`() {

    val firstDocumentId = UUID.randomUUID().toString()
    val firstDocument = saveTag(firstDocumentId, "first tag")
    val secondDocumentId = UUID.randomUUID().toString()
    val secondDocument = saveTag(secondDocumentId, "second tag")
    val thirdDocumentId = UUID.randomUUID().toString()
    val thirdDocument = saveTag(thirdDocumentId, "third tag")

    //
    val tags = repository.findAll(0, 3, "Desc")
    //

    assertThat(tags).isNotEmpty
    assertThat(tags).hasSize(3)

    assertThat(tags[0].key()).isEqualTo(thirdDocumentId)

    val tagsByIds = tags.associateBy { it.key() }

    tagsByIds[firstDocumentId]?.let {
      assertThat(it.value().name).isEqualTo(firstDocument.name)
    }
    tagsByIds[secondDocumentId]?.let {
      assertThat(it.value().name).isEqualTo(secondDocument.name)
    }
    tagsByIds[thirdDocumentId]?.let {
      assertThat(it.value().name).isEqualTo(thirdDocument.name)
    }
  }

  @Test
  fun `should return list of tags with first two elements`() {

    val firstDocumentId = UUID.randomUUID().toString()
    val firstDocument = saveTag(firstDocumentId, "first tag")
    val secondDocumentId = UUID.randomUUID().toString()
    val secondDocument = saveTag(secondDocumentId, "second tag")
    saveTag(UUID.randomUUID().toString(), "third tag")

    //
    val tags = repository.findAll(0, 2, "Asc")
    //

    assertThat(tags).isNotEmpty
    assertThat(tags).hasSize(2)

    assertThat(tags[0].key()).isEqualTo(firstDocumentId)

    val tagsByIds = tags.associateBy { it.key() }

    tagsByIds[firstDocumentId]?.let {
      assertThat(it.value().name).isEqualTo(firstDocument.name)
    }
    tagsByIds[secondDocumentId]?.let {
      assertThat(it.value().name).isEqualTo(secondDocument.name)
    }
  }

  @Test
  fun `should return list of tags with last two elements`() {

    saveTag(UUID.randomUUID().toString(), "first tag")
    val secondDocumentId = UUID.randomUUID().toString()
    val secondDocument = saveTag(secondDocumentId, "second tag")
    val thirdDocumentId = UUID.randomUUID().toString()
    val thirdDocument = saveTag(thirdDocumentId, "third tag")

    //
    val tags = repository.findAll(1, 2, "Asc")
    //

    assertThat(tags).isNotEmpty
    assertThat(tags).hasSize(2)

    assertThat(tags[0].key()).isEqualTo(secondDocumentId)

    val tagsByIds = tags.associateBy { it.key() }

    tagsByIds[secondDocumentId]?.let {
      assertThat(it.value().name).isEqualTo(secondDocument.name)
    }
    tagsByIds[thirdDocumentId]?.let {
      assertThat(it.value().name).isEqualTo(thirdDocument.name)
    }
  }

  @Test
  fun `should create tag`() {

    val record = TagDocument("some tag")

    //
    val id = repository.createTag(record)
    //

    val savedTag = loadTag(id)

    assertThat(savedTag).isNotNull
    savedTag?.let {
      assertThat(it.name).isEqualTo(record.name)
    }
  }

  @Test
  fun `should update tag`() {

    val id = UUID.randomUUID().toString()
    saveTag(id, "some tag")

    val updatedDocument = TagDocument("new name")

    //
    repository.updateTag(id, updatedDocument)
    //

    val updatedTag = loadTag(id)

    assertThat(updatedTag).isNotNull
    updatedTag?.let {
      assertThat(it.name).isEqualTo(updatedDocument.name)
    }
  }

  @Test
  fun `should delete tag`() {

    val id = UUID.randomUUID().toString()
    saveTag(id, "some tag")

    //
    repository.deleteTag(id)
    //

    val updatedTag = loadTag(id)

    assertThat(updatedTag).isNull()
  }

  private fun saveTag(id: String, name: String): TagDocument {
    val document = TagDocument(name)
    client.create {
      it.index(Index.TAGS.code)
          .id(id)
          .refresh(Refresh.True)
          .document(document)
    }
    return document;
  }

  private fun loadTag(id: String): TagDocument? {
    return client.get(
        {
          it.index(Index.TAGS.code)
              .id(id)
        }, TagDocument::class.java)
        .source()
  }

}