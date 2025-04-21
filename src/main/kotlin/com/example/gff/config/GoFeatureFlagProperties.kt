package com.example.gff.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "feature-flag.go-feature-flag")
data class GoFeatureFlagProperties(
    val endpoint: String = "http://localhost:1031/",
    val cache: CacheProperties = CacheProperties()
)

data class CacheProperties(
    val enabled: Boolean = true,
    val expirationTimeMinutes: Long = 1,
    val maximumSize: Long = 1000,
    val initialCapacity: Int = 100
)