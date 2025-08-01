package com.dkb.urlshortener.controller

import com.dkb.urlshortener.dto.ShortenRequestDto
import com.dkb.urlshortener.dto.ShortenResponseDto
import com.dkb.urlshortener.service.UrlShortenerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI

@RestController
@RequestMapping("/api")
class UrlShortenerController(
    private val service: UrlShortenerService
) {

    // POST: Shorten URL → returns 201 Created
    @PostMapping("/shorten")
    fun shortenUrl(@RequestBody request: ShortenRequestDto): ResponseEntity<ShortenResponseDto> {
        val response = service.shortenUrl(request)
        return ResponseEntity.created(URI.create("/api/${response.shortCode}")).body(response)
    }

    // GET: Retrieve original URL → returns 200 OK or 404
    @GetMapping("/{shortCode}")
    fun getOriginalUrl(@PathVariable shortCode: String): ResponseEntity<Map<String, String>> {
        val originalUrl = service.getOriginalUrl(shortCode)
        return ResponseEntity.ok(mapOf("originalUrl" to originalUrl))
    }
}
