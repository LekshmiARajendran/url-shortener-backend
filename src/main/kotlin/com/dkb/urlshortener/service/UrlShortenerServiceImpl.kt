package com.dkb.urlshortener.service

import com.dkb.urlshortener.dto.ShortenRequestDto
import com.dkb.urlshortener.dto.ShortenResponseDto
import com.dkb.urlshortener.model.UrlMapping
import com.dkb.urlshortener.repository.UrlMappingRepository
import org.springframework.beans.factory.annotation.Value
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
        // Validate input URL
        if (!isValidUrl(request.originalUrl)) {
            throw IllegalArgumentException("Invalid URL format")
        }

        // If URL already shortened, return same shortCode
        val existing = repository.findByOriginalUrl(request.originalUrl)
        if (existing != null) {
            return ShortenResponseDto(existing.shortCode)
        }

        // Generate unique shortCode
        val shortCode = generateUniqueShortCode(request.originalUrl)

        // Save new mapping
        val entity = UrlMapping(originalUrl = request.originalUrl, shortCode = shortCode)
        repository.save(entity)

        return ShortenResponseDto(shortCode)
    }

    override fun getOriginalUrl(shortCode: String): String {
        val mapping = repository.findByShortCode(shortCode)
            ?: throw NoSuchElementException("Short URL not found")
        return mapping.originalUrl
    }

    private fun generateUniqueShortCode(url: String): String {
        var code: String
        do {
            code = generateShortCodeWithSalt(url)
        } while (repository.findByShortCode(code) != null)
        return code
    }

    private fun generateShortCodeWithSalt(url: String): String {
        val salt = UUID.randomUUID().toString()
        val saltedUrl = "$salt$url"
        val digest = MessageDigest.getInstance("MD5").digest(saltedUrl.toByteArray())
        return Base64.getUrlEncoder().withoutPadding().encodeToString(digest).take(6)
    }

    private fun isValidUrl(url: String): Boolean {
        return try {
            val parsedUrl = URL(url)
            // Allow only http and https schemes
            parsedUrl.protocol == "http" || parsedUrl.protocol == "https"
        } catch (e: MalformedURLException) {
            false
        }
    }
}
