package com.example.gff.config

import dev.openfeature.contrib.providers.gofeatureflag.GoFeatureFlagProvider
import dev.openfeature.contrib.providers.gofeatureflag.GoFeatureFlagProviderOptions
import dev.openfeature.sdk.Client
import dev.openfeature.sdk.OpenFeatureAPI
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenFeatureConfig {
    @Bean
    fun openFeatureClient(): Client =
        OpenFeatureAPI
            .getInstance()
            .apply {
                provider =
                    GoFeatureFlagProvider(
                        GoFeatureFlagProviderOptions.builder().endpoint("http://localhost:1031/").build(),
                    )
            }.client
}
