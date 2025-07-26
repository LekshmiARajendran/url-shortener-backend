package com.dkb.urlshortener.model

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
@Table(name = "url_mapping")
data class UrlMapping(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @Column(nullable = false)
    val originalUrl: String,

    @Column(nullable = false, unique = true)
    val shortCode: String,

    @Column(nullable = false)
    val createdAt: LocalDateTime = LocalDateTime.now()
)
