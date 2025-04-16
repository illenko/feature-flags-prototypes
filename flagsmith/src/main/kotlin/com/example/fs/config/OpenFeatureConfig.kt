package com.example.fs.config

import dev.openfeature.contrib.providers.flagsmith.FlagsmithProvider
import dev.openfeature.contrib.providers.flagsmith.FlagsmithProviderOptions
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
                    FlagsmithProvider(
                        FlagsmithProviderOptions
                            .builder()
                            .apiKey("MXSYZznJnzzew626a7dDRX")
                            .baseUri("http://localhost:8000/api/v1/")
                            .build(),
                    )
            }.client
}
