package com.dkb.urlshortener.integration

import com.dkb.urlshortener.dto.ShortenRequestDto
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.*
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class UrlShortenerIntegrationTest(
    @Autowired val restTemplate: TestRestTemplate,
    @LocalServerPort val port: Int
) {

    private fun url(path: String) = "http://localhost:$port$path"

    @Test
    fun `should shorten URL and retrieve original`() {
        val request = ShortenRequestDto("https://integration-test.com")
        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        val entity = HttpEntity(request, headers)

        val shortenResponse = restTemplate.postForEntity(url("/api/shorten"), entity, Map::class.java)

        assertEquals(HttpStatus.CREATED, shortenResponse.statusCode)
        val shortCode = shortenResponse.body?.get("shortCode") as String

        val getResponse = restTemplate.exchange(
            url("/api/$shortCode"),
            HttpMethod.GET,
            null,
            Map::class.java
        )

        assertEquals(HttpStatus.OK, getResponse.statusCode)
        assertEquals("https://integration-test.com", getResponse.body?.get("originalUrl"))
    }

    @Test
    fun `should return same short code for duplicate URL`() {
        val request = ShortenRequestDto("https://duplicate-test.com")
        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        val entity = HttpEntity(request, headers)

        val firstResponse = restTemplate.postForEntity(url("/api/shorten"), entity, Map::class.java)
        val secondResponse = restTemplate.postForEntity(url("/api/shorten"), entity, Map::class.java)

        assertEquals(firstResponse.body?.get("shortCode"), secondResponse.body?.get("shortCode"))
    }

    @Test
    fun `should return 404 for invalid short code`() {
        val getResponse = restTemplate.exchange(
            url("/api/invalid"),
            HttpMethod.GET,
            null,
            Map::class.java
        )

        assertEquals(HttpStatus.NOT_FOUND, getResponse.statusCode)
        assertEquals("Short URL not found", getResponse.body?.get("error"))
    }

    @Test
    fun `should return 400 for invalid URL`() {
        val request = ShortenRequestDto("invalid-url")
        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        val entity = HttpEntity(request, headers)

        val response = restTemplate.postForEntity(url("/api/shorten"), entity, Map::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Invalid URL format", response.body?.get("error"))
    }

    @Test
    fun `should return 400 for non-http protocols`() {
        val request = ShortenRequestDto("ftp://example.com")
        val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
        val entity = HttpEntity(request, headers)

        val response = restTemplate.postForEntity(url("/api/shorten"), entity, Map::class.java)

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
        assertEquals("Invalid URL format", response.body?.get("error"))
    }
}
