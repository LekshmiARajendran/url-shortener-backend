package com.dkb.urlshortener.service

import com.dkb.urlshortener.dto.ShortenRequestDto
import com.dkb.urlshortener.model.UrlMapping
import com.dkb.urlshortener.repository.UrlMappingRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UrlShortenerServiceImplTest {

    private val repository = mockk<UrlMappingRepository>(relaxed = true)
    private val service = UrlShortenerServiceImpl(repository, "http://localhost:8080")

    @Test
    fun `shortenUrl generates new short code`() {
        val request = ShortenRequestDto("https://google.com")

        every { repository.findByOriginalUrl(any()) } returns null
        every { repository.findByShortCode(any()) } returns null
        every { repository.save(any()) } answers { firstArg() }

        val result = service.shortenUrl(request)

        assertEquals(6, result.shortCode.length)
        verify { repository.save(any()) }
    }

    @Test
    fun `shortenUrl returns existing short code for duplicate URL`() {
        val existing = UrlMapping(id = 1, originalUrl = "https://google.com", shortCode = "abc123")

        every { repository.findByOriginalUrl("https://google.com") } returns existing

        val result = service.shortenUrl(ShortenRequestDto("https://google.com"))

        assertEquals("abc123", result.shortCode)
        verify(exactly = 0) { repository.save(any()) }
    }

    @Test
    fun `getOriginalUrl returns original url`() {
        val mapping = UrlMapping(id = 1, originalUrl = "https://google.com", shortCode = "abc123")

        every { repository.findByShortCode("abc123") } returns mapping

        val result = service.getOriginalUrl("abc123")

        assertEquals("https://google.com", result)
    }

    @Test
    fun `getOriginalUrl throws exception for invalid short code`() {
        every { repository.findByShortCode("invalid") } returns null

        val exception = assertThrows(NoSuchElementException::class.java) {
            service.getOriginalUrl("invalid")
        }

        assertEquals("Short URL not found", exception.message)
    }

    @Test
    fun `shortenUrl throws exception for invalid URL`() {
        val request = ShortenRequestDto("invalid-url")

        val exception = assertThrows(IllegalArgumentException::class.java) {
            service.shortenUrl(request)
        }

        assertEquals("Invalid URL format", exception.message)
    }

    @Test
    fun `shortenUrl throws exception for non-http protocols`() {
        val request = ShortenRequestDto("ftp://example.com")

        val exception = assertThrows(IllegalArgumentException::class.java) {
            service.shortenUrl(request)
        }

        assertEquals("Invalid URL format", exception.message)
    }
}
