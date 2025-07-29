package com.dkb.urlshortener.repository

import com.dkb.urlshortener.model.UrlMapping
import org.springframework.data.jpa.repository.JpaRepository

interface UrlMappingRepository : JpaRepository<UrlMapping, Long> {
    fun findByShortCode(shortCode: String): UrlMapping?
}
