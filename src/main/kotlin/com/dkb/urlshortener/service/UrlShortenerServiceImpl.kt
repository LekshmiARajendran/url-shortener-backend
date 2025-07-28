package com.dkb.urlshortener.service

import com.dkb.urlshortener.model.UrlMapping
import com.dkb.urlshortener.repository.UrlMappingRepository
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.LocalDateTime
import kotlin.random.Random

@Service
class UrlShortenerServiceImpl(
    private val urlMappingRepository: UrlMappingRepository
) : UrlShortenerService {

    override fun shortenUrl(originalUrl: String): String {
        var shortCode: String

        do {
            // Generate hash with random salt to reduce collisions
            shortCode = generateHashCode(originalUrl + Random.nextInt())
        } while (urlMappingRepository.findByShortCodeIgnoreCase(shortCode) != null)

        // Save mapping
        val urlMapping = UrlMapping(
            originalUrl = originalUrl,
            shortCode = shortCode,
            createdAt = LocalDateTime.now()
        )
        urlMappingRepository.save(urlMapping)

        return shortCode
    }

    override fun getOriginalUrl(shortCode: String): String? {
        return urlMappingRepository.findByShortCodeIgnoreCase(shortCode)?.originalUrl
    }

    private fun generateHashCode(input: String, length: Int = 6): String {
        val bytes = MessageDigest.getInstance("MD5").digest(input.toByteArray())
        val hex = bytes.joinToString("") { "%02x".format(it) }
        return hex.substring(0, length)
    }
}
