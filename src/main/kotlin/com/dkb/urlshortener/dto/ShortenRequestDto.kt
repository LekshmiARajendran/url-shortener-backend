package com.dkb.urlshortener.dto

import jakarta.validation.constraints.NotBlank


data class ShortenRequestDto(
    @field:NotBlank(message = "URL must not be blank")
    val url: String
)
