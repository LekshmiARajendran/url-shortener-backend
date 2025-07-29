package com.dkb.urlshortener.repository

import com.dkb.urlshortener.model.UrlMapping
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class UrlMappingRepositoryTest @Autowired constructor(
    val repository: UrlMappingRepository
) {

    @Test
    fun `should save and retrieve by original url`() {
        val entity = UrlMapping(originalUrl = "https://google.com", shortCode = "abc123")
        val saved = repository.save(entity)

        val found = repository.findByOriginalUrl("https://google.com")
        assertNotNull(found)
        assertEquals(saved.id, found?.id)
    }

    @Test
    fun `should save and retrieve by short code`() {
        val entity = UrlMapping(originalUrl = "https://example.com", shortCode = "xyz789")
        val saved = repository.save(entity)

        val found = repository.findByShortCode("xyz789")
        assertNotNull(found)
        assertEquals(saved.originalUrl, found?.originalUrl)
    }
}
