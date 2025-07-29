package com.dkb.urlshortener.service

import com.dkb.urlshortener.dto.ShortenRequestDto
import com.dkb.urlshortener.model.UrlMapping
import com.dkb.urlshortener.repository.UrlMappingRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class UrlShortenerServiceImplTest {

    private val urlMappingRepository = mockk<UrlMappingRepository>(relaxed = true)
    private val urlShortenerService = UrlShortenerServiceImpl(urlMappingRepository)

    @Test
    fun `shortenUrl should handle collision and generate new code`() {
        // Arrange
        val existingMapping = UrlMapping(
            id = 2,
            originalUrl = "https://collision.com",
            shortCode = "1Z4OH1",
            createdAt = LocalDateTime.now()
        )

        // First call returns collision, second returns null
        every { urlMappingRepository.findByShortCode(any()) } returns existingMapping andThen null
        every { urlMappingRepository.save(any()) } answers { firstArg() }

        // Act
        val result = urlShortenerService.shortenUrl(
            ShortenRequestDto(url = "https://collision.com")
        )

        // Assert
        assertNotNull(result.shortCode)
        assertEquals("https://collision.com", result.originalUrl)

        // Verify: findByShortCode called at least twice, save called once
        verify(atLeast = 2) { urlMappingRepository.findByShortCode(any()) }
        verify { urlMappingRepository.save(any()) }
    }
}
