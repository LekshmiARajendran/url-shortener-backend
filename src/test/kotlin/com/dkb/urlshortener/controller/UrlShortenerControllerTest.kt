package com.dkb.urlshortener.controller

import com.dkb.urlshortener.dto.ShortenRequestDto
import com.dkb.urlshortener.dto.ShortenResponseDto
import com.dkb.urlshortener.dto.OriginalUrlResponse
import com.dkb.urlshortener.exception.UrlNotFoundException
import com.dkb.urlshortener.service.UrlShortenerService
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import com.fasterxml.jackson.databind.ObjectMapper

@WebMvcTest(UrlShortenerController::class)
class UrlShortenerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockitoBean
    private lateinit var service: UrlShortenerService

    private val objectMapper = ObjectMapper()

    // ---------- Positive Tests ----------

    @Test
    fun `shortenUrl should return short code`() {
        val request = ShortenRequestDto("https://example.com")
        val response = ShortenResponseDto("abc123")

        Mockito.`when`(service.shortenUrl(request)).thenReturn(response)

        mockMvc.perform(
            post("/api/shorten") // Updated path with /api
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.shortCode").value("abc123"))
    }

    @Test
    fun `getOriginalUrl should return original URL`() {
        val response = OriginalUrlResponse("https://example.com")

        Mockito.`when`(service.getOriginalUrl("abc123")).thenReturn(response)

        mockMvc.perform(get("/api/original/abc123")) // Updated path
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.originalUrl").value("https://example.com"))
    }

    @Test
    fun `getAllUrls should return list of URLs`() {
        val responseList = listOf(
            OriginalUrlResponse("https://a.com"),
            OriginalUrlResponse("https://b.com")
        )

        Mockito.`when`(service.getAllUrls()).thenReturn(responseList)

        mockMvc.perform(get("/api/urls")) // Updated path
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].originalUrl").value("https://a.com"))
    }

    // ---------- Negative Tests ----------

    @Test
    fun `getOriginalUrl should return 404 when URL not found`() {
        Mockito.`when`(service.getOriginalUrl("invalid"))
            .thenThrow(UrlNotFoundException("No URL found for short code: invalid"))

        mockMvc.perform(get("/api/original/invalid")) // Updated path
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.error").value("No URL found for short code: invalid"))
    }

    @Test
    fun `shortenUrl should return 400 for invalid input`() {
        val request = ShortenRequestDto("") // empty URL (invalid)

        mockMvc.perform(
            post("/api/shorten") // Updated path
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
    }
}
