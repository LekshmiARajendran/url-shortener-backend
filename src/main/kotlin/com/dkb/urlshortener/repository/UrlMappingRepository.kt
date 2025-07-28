package com.dkb.urlshortener.repository

import com.dkb.urlshortener.model.UrlMapping
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UrlMappingRepository : JpaRepository<UrlMapping, Long> {

    // Finds the URL mapping ignoring case of the short code
    fun findByShortCodeIgnoreCase(shortCode: String): UrlMapping?
}
