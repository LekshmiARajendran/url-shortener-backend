package com.dkb.urlshortener.controller

import com.dkb.urlshortener.dto.ShortenRequestDto
import com.dkb.urlshortener.dto.ShortenResponseDto
import com.dkb.urlshortener.service.UrlShortenerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class UrlShortenerController(
    private val service: UrlShortenerService
) {

    /**
     * POST /api/shorten
     * Request Body: { "originalUrl": "https://example.com" }
     * Response: { "shortCode": "abc123" }
     */
    @PostMapping("/shorten")
    fun shortenUrl(@RequestBody request: ShortenRequestDto): ResponseEntity<ShortenResponseDto> {
        val response = service.shortenUrl(request)
        return ResponseEntity.ok(response)
    }

    /**
     * GET /api/{shortCode}
     * Response: { "originalUrl": "https://example.com" }
     */
    @GetMapping("/{shortCode}")
    fun getOriginalUrl(@PathVariable shortCode: String): ResponseEntity<Map<String, String>> {
        val originalUrl = service.getOriginalUrl(shortCode)
        return ResponseEntity.ok(mapOf("originalUrl" to originalUrl))
    }
}
