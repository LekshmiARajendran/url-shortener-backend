package com.dkb.urlshortener.service

import com.dkb.urlshortener.dto.OriginalUrlResponse
import com.dkb.urlshortener.dto.ShortenRequestDto
import com.dkb.urlshortener.dto.ShortenResponseDto
import com.dkb.urlshortener.exception.UrlNotFoundException
import com.dkb.urlshortener.model.UrlMapping
import com.dkb.urlshortener.repository.UrlMappingRepository
import org.springframework.stereotype.Service
import java.security.MessageDigest

@Service
class UrlShortenerServiceImpl(
    private val urlMappingRepository: UrlMappingRepository
) : UrlShortenerService {

    override fun shortenUrl(request: ShortenRequestDto): ShortenResponseDto {
        val originalUrl = request.originalUrl

        // Hash using MD5 and take first 6 chars
        val md5Hash = MessageDigest.getInstance("MD5").digest(originalUrl.toByteArray())
        var shortCode = md5Hash.joinToString("") { "%02x".format(it) }.substring(0, 6)

        var attempts = 0
        val maxAttempts = 5

        while (urlMappingRepository.findByShortCode(shortCode) != null && attempts < maxAttempts) {
            val randomExtra = (0..9).random().toString()
            shortCode = (shortCode + randomExtra).take(6)
            attempts++
        }

        // If still collides after maxAttempts, regenerate entirely
        if (urlMappingRepository.findByShortCode(shortCode) != null) {
            shortCode = (100000..999999).random().toString() // fallback random 6-digit code
        }

        val urlMapping = UrlMapping(
            originalUrl = originalUrl,
            shortCode = shortCode
        )
        urlMappingRepository.save(urlMapping)

        return ShortenResponseDto(shortCode = shortCode)
    }

    override fun getOriginalUrl(shortCode: String): OriginalUrlResponse {
        val mapping = urlMappingRepository.findByShortCode(shortCode)
            ?: throw UrlNotFoundException("No URL found for short code: $shortCode")

        return OriginalUrlResponse(originalUrl = mapping.originalUrl)
    }

    // New method to return all stored URLs
    override fun getAllUrls(): List<OriginalUrlResponse> {
        return urlMappingRepository.findAll().map {
            OriginalUrlResponse(it.originalUrl)
        }
    }
}
