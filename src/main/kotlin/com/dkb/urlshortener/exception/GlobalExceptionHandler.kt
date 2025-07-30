package com.dkb.urlshortener.exception

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    // Handle short code not found (404)
    @ExceptionHandler(UrlNotFoundException::class)
    fun handleUrlNotFound(ex: UrlNotFoundException): ResponseEntity<Map<String, String>> {
        return ResponseEntity(mapOf("error" to ex.message!!), HttpStatus.NOT_FOUND)
    }

    // Handle bad input (400)
    @ExceptionHandler(IllegalArgumentException::class)
    fun handleBadRequest(ex: IllegalArgumentException): ResponseEntity<Map<String, String>> {
        return ResponseEntity(mapOf("error" to ex.message!!), HttpStatus.BAD_REQUEST)
    }
}
