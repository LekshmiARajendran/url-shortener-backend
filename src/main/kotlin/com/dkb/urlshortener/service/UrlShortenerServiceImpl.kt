package com.dkb.urlshortener.service

import com.dkb.urlshortener.dto.ShortenRequestDto
import com.dkb.urlshortener.dto.ShortenResponseDto
import com.dkb.urlshortener.model.UrlMapping
import com.dkb.urlshortener.repository.UrlMappingRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import java.net.MalformedURLException
import java.net.URL
import java.security.MessageDigest
import java.util.*

@Service
class UrlShortenerServiceImpl(
    private val repository: UrlMappingRepository,
    @Value("\${app.base-url}") private val baseUrl: String
) : UrlShortenerService {

    override fun shortenUrl(request: ShortenRequestDto): ShortenResponseDto {
        // Validate input URL format
        if (!isValidUrl(request.originalUrl)) {
            throw IllegalArgumentException("Invalid URL format")
        }

        var entity: UrlMapping? = null
        var saved = false

        // Retry loop to handle rare short code collisions
        while (!saved) {
            try {
                val shortCode = generateUniqueShortCode(request.originalUrl)

                // Save mapping (no uniqueness on original URL, unique on shortCode)
                entity = repository.save(UrlMapping(originalUrl = request.originalUrl, shortCode = shortCode))
                saved = true
            } catch (ex: DataIntegrityViolationException) {
                // If collision occurs (duplicate shortCode), retry with new salt
                continue
            }
        }

        // Return ONLY the short code (Postman expects this)
        return ShortenResponseDto(entity!!.shortCode)
    }

    override fun getOriginalUrl(shortCode: String): String {
        val mapping = repository.findByShortCode(shortCode)
            ?: throw IllegalArgumentException("Short URL not found")
        return mapping.originalUrl
    }

    /**
     * Generates a unique short code using random salt + MD5 hash + Base64 (6 chars)
     */
    private fun generateUniqueShortCode(url: String): String {
        var code: String
        do {
            code = generateShortCodeWithSalt(url)
        } while (repository.findByShortCode(code) != null) // extra safety
        return code
    }

    private fun generateShortCodeWithSalt(url: String): String {
        val salt = UUID.randomUUID().toString() // random salt
        val saltedUrl = "$salt$url"
        val digest = MessageDigest.getInstance("MD5").digest(saltedUrl.toByteArray())
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest).take(6)
    }

    private fun isValidUrl(url: String): Boolean {
        return try {
            URL(url)
            true
        } catch (e: MalformedURLException) {
            false
        }
    }
}
