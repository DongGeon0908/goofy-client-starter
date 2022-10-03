package com.goofy.client.starter.autoconfigure

import com.goofy.client.starter.webclient.WebClientFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.web.reactive.function.client.WebClient

@Primary
@Configuration
class WebClientAutoConfigure {
    companion object {
        const val DEFAULT_BASE_URL = ""
    }

    @Primary
    @Bean
    fun webClient(
        config: WebClientConfig
    ): WebClient {
        return WebClientFactory().webClient(
            baseUrl = DEFAULT_BASE_URL,
            connectionTimeoutMillis = config.connectionTimeoutMillis,
            readTimeoutMillis = config.readTimeoutMillis,
            writeTimeoutMillis = config.writeTimeoutMillis
        )
    }

    /**
     * Webclient Config
     **/
    @Primary
    @Configuration
    @ConfigurationProperties(prefix = "goofy.starter.client.webclient")
    @ConfigurationPropertiesBinding
    class WebClientConfig {
        var connectionTimeoutMillis: Int = 10_000
        var readTimeoutMillis: Long = 10_000
        var writeTimeoutMillis: Long = 10_000
    }
}
