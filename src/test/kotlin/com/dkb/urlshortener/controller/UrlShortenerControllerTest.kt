package com.dkb.urlshortener.service

import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.extension.ExtendWith
import com.dkb.urlshortener.dto.ShortenRequestDto
import com.dkb.urlshortener.exception.UrlNotFoundException
import com.dkb.urlshortener.model.UrlMapping
import com.dkb.urlshortener.repository.UrlMappingRepository
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@ExtendWith(MockKExtension::class)
class UrlShortenerServiceImplTest {

    private val repository: UrlMappingRepository = mockk(relaxed = true)
    private val service = UrlShortenerServiceImpl(repository)

    // ---------- shortenUrl() Tests ----------

    @Test
    fun `shortenUrl should generate short code and save mapping`() {
        val request = ShortenRequestDto("https://example.com")

        every { repository.findByShortCode(any()) } returns null
        every { repository.save(any()) } returns UrlMapping(1L, "https://example.com", "abc123")

        val result = service.shortenUrl(request)

        assertEquals(6, result.shortCode.length) // Validate length only
        verify { repository.save(any()) }
    }

    @Test
    fun `shortenUrl should handle collision and generate new code`() {
        val request = ShortenRequestDto("https://collision.com")

        // Simulate first collision, then success
        every { repository.findByShortCode(any()) }
            .returns(UrlMapping(1L, "https://existing.com", "abc123")) // collision
            .andThen(null) // success on retry

        every { repository.save(any()) } returns UrlMapping(2L, "https://collision.com", "xyz456")

        val result = service.shortenUrl(request)

        assertEquals(6, result.shortCode.length)
        verify { repository.save(any()) } // Only check save called (not count)
    }

    @Test
    fun `shortenUrl should create unique short codes for different URLs`() {
        val request1 = ShortenRequestDto("https://example1.com")
        val request2 = ShortenRequestDto("https://example2.com")

        every { repository.findByShortCode(any()) } returns null
        every { repository.save(any()) } answers { firstArg<UrlMapping>() }

        val result1 = service.shortenUrl(request1)
        val result2 = service.shortenUrl(request2)

        assertEquals(6, result1.shortCode.length)
        assertEquals(6, result2.shortCode.length)
        assertNotEquals(result1.shortCode, result2.shortCode)
    }

    // ---------- getOriginalUrl() Tests ----------

    @Test
    fun `getOriginalUrl should return URL when found`() {
        every { repository.findByShortCode("abc123") } returns UrlMapping(1L, "https://example.com", "abc123")

        val result = service.getOriginalUrl("abc123")

        assertEquals("https://example.com", result.originalUrl)
    }

    @Test
    fun `getOriginalUrl should throw exception when not found`() {
        every { repository.findByShortCode("invalid") } returns null

        val exception = assertThrows<UrlNotFoundException> {
            service.getOriginalUrl("invalid")
        }

        assertEquals("No URL found for short code: invalid", exception.message)
    }

    // ---------- getAllUrls() Tests ----------

    @Test
    fun `getAllUrls should return list of OriginalUrlResponse`() {
        every { repository.findAll() } returns listOf(
            UrlMapping(1L, "https://a.com", "aaa111"),
            UrlMapping(2L, "https://b.com", "bbb222")
        )

        val result = service.getAllUrls()

        assertEquals(2, result.size)
        assertEquals("https://a.com", result[0].originalUrl)
    }

    @Test
    fun `getAllUrls should return empty list when no URLs exist`() {
        every { repository.findAll() } returns emptyList()

        val result = service.getAllUrls()

        assertTrue(result.isEmpty())
    }
}
