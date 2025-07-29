package com.dkb.urlshortener.dto

data class ShortenResponseDto(
    val shortCode: String,
    val originalUrl: String
)
