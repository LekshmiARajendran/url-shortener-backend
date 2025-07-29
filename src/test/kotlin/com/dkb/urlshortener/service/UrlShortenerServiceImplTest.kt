package com.dkb.urlshortener.service

import com.dkb.urlshortener.dto.ShortenRequestDto
import com.dkb.urlshortener.model.UrlMapping
import com.dkb.urlshortener.repository.UrlMappingRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UrlShortenerServiceImplTest {

    private val repository = mockk<UrlMappingRepository>(relaxed = true)
    private val service = UrlShortenerServiceImpl(repository, "http://localhost:8080/api/")

    @Test
    fun `shortenUrl generates new short code`() {
        val request = ShortenRequestDto("https://google.com")

        every { repository.findByOriginalUrl(any()) } returns null
        every { repository.findByShortCode(any()) } returns null
        every { repository.save(any()) } answers { firstArg() }

        val result = service.shortenUrl(request)

        assertEquals(6, result.shortCode.length) // shortCode should be 6 chars
        verify { repository.save(any()) }
    }

    @Test
    fun `getOriginalUrl returns original url`() {
        val mapping = UrlMapping(
            id = 1,
            originalUrl = "https://google.com",
            shortCode = "abc123"
        )

        every { repository.findByShortCode("abc123") } returns mapping

        val result = service.getOriginalUrl("abc123")

        assertEquals("https://google.com", result)
        verify { repository.findByShortCode("abc123") }
    }
}
