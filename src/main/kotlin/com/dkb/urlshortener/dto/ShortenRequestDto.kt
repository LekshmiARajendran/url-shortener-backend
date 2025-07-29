package com.dkb.urlshortener.dto

import jakarta.validation.constraints.NotBlank

data class ShortenRequestDto(
    @field:NotBlank
    val originalUrl: String
)
