package com.dkb.urlshortener.repository

import com.dkb.urlshortener.model.UrlMapping
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles
import java.time.LocalDateTime
import org.junit.jupiter.api.assertThrows

@DataJpaTest
@ActiveProfiles("test")
class UrlMappingRepositoryTest @Autowired constructor(
    val repository: UrlMappingRepository
) {

    @BeforeEach
    fun clearDatabase() {
        // Ensures test DB is clean before each test
        repository.deleteAll()
    }

    @Test
    fun `should save and retrieve UrlMapping by shortCode`() {
        val mapping = UrlMapping(
            originalUrl = "https://example.com",
            shortCode = "abc123",
            createdAt = LocalDateTime.now()
        )
        repository.save(mapping)

        val found = repository.findByShortCode("abc123")
        assertNotNull(found)
        assertEquals("https://example.com", found?.originalUrl)
    }

    @Test
    fun `should return null when shortCode does not exist`() {
        val result = repository.findByShortCode("nonexistent")
        assertNull(result)
    }

    @Test
    fun `should retrieve all saved UrlMappings`() {
        repository.save(UrlMapping(originalUrl = "https://a.com", shortCode = "a11111"))
        repository.save(UrlMapping(originalUrl = "https://b.com", shortCode = "b22222"))

        val all = repository.findAll()
        assertEquals(2, all.size) // Now guaranteed to have only 2 entries
    }

    @Test
    fun `should enforce unique shortCode`() {
        repository.save(UrlMapping(originalUrl = "https://first.com", shortCode = "dup123"))

        val exception = assertThrows<Exception> {
            repository.save(UrlMapping(originalUrl = "https://second.com", shortCode = "dup123"))
        }

        assertTrue(
            exception.message?.contains("duplicate", ignoreCase = true) == true ||
                    exception.message?.contains("constraint", ignoreCase = true) == true
        )
    }
}
