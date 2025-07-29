package com.dkb.urlshortener.service

import com.dkb.urlshortener.dto.ShortenRequestDto
import com.dkb.urlshortener.dto.ShortenResponseDto
import com.dkb.urlshortener.dto.OriginalUrlResponse
import com.dkb.urlshortener.model.UrlMapping
import com.dkb.urlshortener.repository.UrlMappingRepository
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

@Service
class UrlShortenerServiceImpl(
    private val urlMappingRepository: UrlMappingRepository
) : UrlShortenerService {

    override fun shortenUrl(request: ShortenRequestDto): ShortenResponseDto {
        val originalUrl = request.originalUrl

        // Validate URL
        if (!isValidUrl(originalUrl)) {
            throw IllegalArgumentException("Invalid URL format")
        }

        // Generate short code
        var shortCode: String
        do {
            shortCode = generateShortCode(originalUrl)
        } while (urlMappingRepository.findByShortCode(shortCode) != null)

        // Save mapping
        val mapping = UrlMapping(
            originalUrl = originalUrl,
            shortCode = shortCode
        )
        urlMappingRepository.save(mapping)

        return ShortenResponseDto(
            shortCode = shortCode,
            originalUrl = originalUrl
        )
    }

    override fun getOriginalUrl(shortCode: String): OriginalUrlResponse? {
        val mapping = urlMappingRepository.findByShortCode(shortCode)
        return mapping?.let { OriginalUrlResponse(it.originalUrl) }
    }

    // ---- Helper methods ----

    private fun isValidUrl(url: String): Boolean {
        val urlRegex = Regex("^(https?://)?([\\w.-]+)\\.([a-z]{2,6})([/\\w .-]*)*/?$")
        return urlRegex.matches(url)
    }

    private fun generateShortCode(originalUrl: String): String {
        val salt = ByteArray(8)
        SecureRandom().nextBytes(salt)
        val saltedUrl = salt.toString(Charsets.UTF_8) + originalUrl

        // MD5 hash
        val md5Digest = MessageDigest.getInstance("MD5")
        val hash = md5Digest.digest(saltedUrl.toByteArray())

        // Base64 encode and take first 6 chars
        return Base64.getUrlEncoder().withoutPadding().encodeToString(hash).take(6)
    }
}
