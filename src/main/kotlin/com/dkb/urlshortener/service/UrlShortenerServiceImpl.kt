package com.dkb.urlshortener.service

import com.dkb.urlshortener.exception.UrlNotFoundException
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
        var shortCode = generateHashCode(originalUrl)

        // Keep generating a new random code if there is a collision
        while (urlMappingRepository.findByShortCodeIgnoreCase(shortCode) != null) {
            shortCode = generateRandomCode()
        }

        val urlMapping = UrlMapping(
            originalUrl = originalUrl,
            shortCode = shortCode,
            createdAt = LocalDateTime.now()
        )
        urlMappingRepository.save(urlMapping)
        return shortCode
    }

    override fun getOriginalUrl(shortCode: String): String {
        val urlMapping = urlMappingRepository.findByShortCodeIgnoreCase(shortCode)
            ?: throw UrlNotFoundException("No URL found for short code: $shortCode")

        return urlMapping.originalUrl
    }

    private fun generateHashCode(originalUrl: String, length: Int = 6): String {
        val bytes = MessageDigest.getInstance("MD5").digest(originalUrl.toByteArray())
        val hex = bytes.joinToString("") { "%02x".format(it) }
        return hex.substring(0, length)
    }

    private fun generateRandomCode(length: Int = 6): String {
        val chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
        return (1..length)
            .map { chars[Random.nextInt(chars.length)] }
            .joinToString("")
    }
}
