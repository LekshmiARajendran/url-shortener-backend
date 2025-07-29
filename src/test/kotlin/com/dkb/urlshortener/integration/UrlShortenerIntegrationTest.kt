package com.dkb.urlshortener.integration

import com.dkb.urlshortener.dto.ShortenRequestDto
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.result.MockMvcResultHandlers.print

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UrlShortenerIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    @Test
    fun `full flow - shorten URL and retrieve it`() {
        // Step 1: Shorten URL
        val request = ShortenRequestDto("https://integration-test.com")

        val shortenResponse = mockMvc.perform(
            post("/api/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.shortCode").exists())
            .andReturn()

        // Extract shortCode from JSON
        val jsonResponse = shortenResponse.response.contentAsString
        val shortCode = objectMapper.readTree(jsonResponse).get("shortCode").asText()

        // Step 2: Retrieve original URL
        mockMvc.perform(get("/api/original/$shortCode"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.originalUrl").value("https://integration-test.com"))
    }

    @Test
    fun `get all URLs should return list including newly added URL`() {
        // Add one URL
        val request = ShortenRequestDto("https://list-test.com")

        mockMvc.perform(
            post("/api/shorten")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        )
            .andExpect(status().isOk)

        // Get all URLs
        mockMvc.perform(get("/api/urls"))
            .andDo(print())
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(1)))
    }
}
