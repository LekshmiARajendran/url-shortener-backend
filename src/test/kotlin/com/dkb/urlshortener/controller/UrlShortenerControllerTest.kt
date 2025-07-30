package com.dkb.urlshortener.controller

import com.dkb.urlshortener.dto.ShortenRequestDto
import com.dkb.urlshortener.dto.ShortenResponseDto
import com.dkb.urlshortener.service.UrlShortenerService
import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.verify
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import com.fasterxml.jackson.databind.ObjectMapper

@WebMvcTest(UrlShortenerController::class)
class UrlShortenerControllerTest(
    @Autowired val mockMvc: MockMvc,
    @Autowired val objectMapper: ObjectMapper
) {

    @MockkBean
    lateinit var service: UrlShortenerService

    @Test
    fun `POST shorten returns 201 and shortCode`() {
        val request = ShortenRequestDto("https://google.com")
        val response = ShortenResponseDto("abc123")

        every { service.shortenUrl(any()) } returns response

        mockMvc.perform(
            post("/api/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.shortCode").value("abc123"))

        verify { service.shortenUrl(any()) }
    }

    @Test
    fun `POST shorten returns 400 for invalid URL`() {
        val request = ShortenRequestDto("invalid-url")
        every { service.shortenUrl(any()) } throws IllegalArgumentException("Invalid URL format")

        mockMvc.perform(
            post("/api/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Invalid URL format"))
    }

    @Test
    fun `POST shorten returns 400 for missing body`() {
        mockMvc.perform(
            post("/api/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}")
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Request body is missing or malformed"))
    }

    @Test
    fun `POST shorten returns 400 for non-http protocols`() {
        val request = ShortenRequestDto("ftp://example.com")
        every { service.shortenUrl(any()) } throws IllegalArgumentException("Invalid URL format")

        mockMvc.perform(
            post("/api/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.error").value("Invalid URL format"))
    }

    @Test
    fun `GET original returns originalUrl`() {
        every { service.getOriginalUrl("abc123") } returns "https://google.com"

        mockMvc.perform(
            get("/api/abc123")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.originalUrl").value("https://google.com"))
    }

    @Test
    fun `GET returns 404 for invalid short code`() {
        every { service.getOriginalUrl("invalid") } throws NoSuchElementException("Short URL not found")

        mockMvc.perform(
            get("/api/invalid")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.error").value("Short URL not found"))
    }

    @Test
    fun `wrong HTTP method returns 405`() {
        mockMvc.perform(
            put("/api/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ShortenRequestDto("https://google.com")))
        )
            .andExpect(status().isMethodNotAllowed)
            .andExpect(jsonPath("$.error").value("Method not allowed"))
    }
}
