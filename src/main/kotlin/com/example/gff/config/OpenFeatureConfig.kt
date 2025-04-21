package com.example.gff.config

import com.github.benmanes.caffeine.cache.Caffeine
import dev.openfeature.contrib.providers.gofeatureflag.GoFeatureFlagProvider
import dev.openfeature.contrib.providers.gofeatureflag.GoFeatureFlagProviderOptions
import dev.openfeature.sdk.Client
import dev.openfeature.sdk.OpenFeatureAPI
import dev.openfeature.sdk.ProviderEvaluation
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class OpenFeatureConfig(private val properties: GoFeatureFlagProperties) {
    @Bean
    fun openFeatureClient(): Client {
        val cache =
            Caffeine
                .newBuilder()
                .expireAfterWrite(properties.cache.expirationTimeMinutes, TimeUnit.MINUTES)
                .maximumSize(properties.cache.maximumSize)
                .initialCapacity(properties.cache.initialCapacity) as Caffeine<String, ProviderEvaluation<*>>

        return OpenFeatureAPI
            .getInstance()
            .apply {
                provider =
                    GoFeatureFlagProvider(
                        GoFeatureFlagProviderOptions
                            .builder()
                            .endpoint(properties.endpoint)
                            .enableCache(properties.cache.enabled)
                            .cacheConfig(cache)
                            .build(),
                    )
            }.client
    }
}
