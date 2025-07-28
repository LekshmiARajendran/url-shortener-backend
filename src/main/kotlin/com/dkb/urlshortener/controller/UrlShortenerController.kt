package com.dkb.urlshortener.controller

import com.dkb.urlshortener.dto.ShortenRequestDto
import com.dkb.urlshortener.dto.ShortenResponseDto
import com.dkb.urlshortener.service.UrlShortenerService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class UrlShortenerController(
    private val urlShortenerService: UrlShortenerService
) {

    // POST /api/shorten
    @PostMapping("/shorten")
    fun shortenUrl(@RequestBody request: ShortenRequestDto): ShortenResponseDto {
        val shortCode = urlShortenerService.shortenUrl(request.originalUrl)
        return ShortenResponseDto(shortCode)
    }

    // GET /api/{shortCode}
    @GetMapping("/{shortCode}")
    fun getOriginalUrl(@PathVariable shortCode: String): Map<String, String?> {
        val originalUrl = urlShortenerService.getOriginalUrl(shortCode)
        return mapOf("originalUrl" to originalUrl)
    }
}
