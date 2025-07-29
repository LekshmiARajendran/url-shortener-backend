package com.dkb.urlshortener.controller

import com.dkb.urlshortener.dto.ShortenRequestDto
import com.dkb.urlshortener.dto.ShortenResponseDto
import com.dkb.urlshortener.dto.OriginalUrlResponse
import com.dkb.urlshortener.service.UrlShortenerService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api")
class UrlShortenerController(
    private val urlShortenerService: UrlShortenerService
) {

    @PostMapping("/shorten")
    fun shortenUrl(@RequestBody request: ShortenRequestDto): ResponseEntity<Any> {
        return try {
            val response = urlShortenerService.shortenUrl(request)
            ResponseEntity.ok(response)
        } catch (ex: IllegalArgumentException) {
            ResponseEntity.status(HttpStatus.BAD_REQUEST).body(mapOf("error" to ex.message))
        }
    }

    @GetMapping("/{shortCode}")
    fun getOriginalUrl(@PathVariable shortCode: String): ResponseEntity<Any> {
        val response = urlShortenerService.getOriginalUrl(shortCode)
        return if (response != null) {
            ResponseEntity.ok(response)
        } else {
            ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(mapOf("error" to "Short code not found"))
        }
    }
}
