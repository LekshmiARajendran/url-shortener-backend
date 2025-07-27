package com.dkb.urlshortener.service

interface UrlShortenerService {
    fun shortenUrl(originalUrl: String): String
    fun getOriginalUrl(shortCode: String): String?
}
