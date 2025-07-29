package com.dkb.urlshortener.controller

import com.dkb.urlshortener.dto.OriginalUrlResponse
import com.dkb.urlshortener.dto.ShortenRequestDto
import com.dkb.urlshortener.dto.ShortenResponseDto
import com.dkb.urlshortener.service.UrlShortenerService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class UrlShortenerController(
    private val urlShortenerService: UrlShortenerService
) {

    @PostMapping("/shorten")
    fun shortenUrl(@Valid @RequestBody request: ShortenRequestDto): ResponseEntity<ShortenResponseDto> {
        val response = urlShortenerService.shortenUrl(request)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/original/{shortCode}")
    fun getOriginalUrl(@PathVariable shortCode: String): ResponseEntity<OriginalUrlResponse> {
        val response = urlShortenerService.getOriginalUrl(shortCode)
        return ResponseEntity.ok(response)
    }

    @GetMapping("/urls")
    fun getAllUrls(): ResponseEntity<List<OriginalUrlResponse>> {
        val response = urlShortenerService.getAllUrls()
        return ResponseEntity.ok(response)
    }
}
