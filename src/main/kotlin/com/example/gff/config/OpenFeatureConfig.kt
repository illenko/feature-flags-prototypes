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
class OpenFeatureConfig {
    @Bean
    fun openFeatureClient(): Client {
        val cache =
            Caffeine
                .newBuilder()
                .expireAfterWrite(1, TimeUnit.MINUTES)
                .maximumSize(1000)
                .initialCapacity(100) as Caffeine<String, ProviderEvaluation<*>>

        return OpenFeatureAPI
            .getInstance()
            .apply {
                provider =
                    GoFeatureFlagProvider(
                        GoFeatureFlagProviderOptions
                            .builder()
                            .endpoint("http://localhost:1031/")
                            .enableCache(true)
                            .cacheConfig(cache)
                            .build(),
                    )
            }.client
    }
}
