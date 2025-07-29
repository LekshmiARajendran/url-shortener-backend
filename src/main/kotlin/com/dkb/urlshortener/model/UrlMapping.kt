package com.dkb.urlshortener.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "url_mapping")   // removed unique constraint on original_url
data class UrlMapping(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(name = "original_url", nullable = false)  // no unique constraint
    val originalUrl: String,

    @Column(name = "short_code", nullable = false, unique = true) // shortcode stays unique
    val shortCode: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
