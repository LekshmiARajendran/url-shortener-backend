package com.dkb.urlshortener.service

import com.dkb.urlshortener.dto.ShortenRequestDto
import com.dkb.urlshortener.dto.ShortenResponseDto
import com.dkb.urlshortener.dto.OriginalUrlResponse

interface UrlShortenerService {
    fun shortenUrl(request: ShortenRequestDto): ShortenResponseDto
    fun getOriginalUrl(shortCode: String): OriginalUrlResponse?
}
