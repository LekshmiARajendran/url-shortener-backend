package com.dkb.urlshortener.repository

import com.dkb.urlshortener.model.UrlMapping
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface UrlMappingRepository : JpaRepository<UrlMapping, Long> {

    @Query("SELECT u FROM UrlMapping u WHERE LOWER(u.shortCode) = LOWER(:shortCode)")
    fun findByShortCode(@Param("shortCode") shortCode: String): UrlMapping?
}
