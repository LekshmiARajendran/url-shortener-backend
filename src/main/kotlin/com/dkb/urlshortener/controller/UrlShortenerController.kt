package com.dkb.urlshortener.controller

import com.dkb.urlshortener.service.UrlShortenerService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class UrlShortenerController(
    private val urlShortenerService: UrlShortenerService
) {

    data class ShortenRequest(val originalUrl: String)
    data class ShortenResponse(val shortCode: String)
    data class OriginalUrlResponse(val originalUrl: String?)

    // POST /api/shorten
    @PostMapping("/shorten")
    fun shortenUrl(@RequestBody request: ShortenRequest): ShortenResponse {
        val shortCode = urlShortenerService.shortenUrl(request.originalUrl)
        return ShortenResponse(shortCode)
    }

    // GET /api/{shortCode}
    @GetMapping("/{shortCode}")
    fun getOriginalUrl(@PathVariable shortCode: String): OriginalUrlResponse {
        val originalUrl = urlShortenerService.getOriginalUrl(shortCode)
        return OriginalUrlResponse(originalUrl)
    }
}
