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
    fun `POST shorten returns shortCode`() {
        val request = ShortenRequestDto("https://google.com")
        val response = ShortenResponseDto("abc123")

        every { service.shortenUrl(any()) } returns response

        mockMvc.perform(
            post("/api/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.shortCode").value("abc123"))

        verify { service.shortenUrl(any()) }
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
    fun `GET original alternative endpoint works`() {
        every { service.getOriginalUrl("abc123") } returns "https://google.com"

        mockMvc.perform(
            get("/api/original/abc123")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.originalUrl").value("https://google.com"))
    }

    @Test
    fun `GET returns 404 for invalid short code`() {
        every { service.getOriginalUrl("invalid") } throws UrlNotFoundException("no url found for invalid")

        mockMvc.perform(
            get("/api/original/invalid")
                .accept(MediaType.APPLICATION_JSON)
        )
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.error").value("no url found for invalid"))
    }
}
