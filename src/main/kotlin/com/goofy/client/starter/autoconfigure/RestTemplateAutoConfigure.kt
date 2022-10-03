package com.goofy.client.starter.autoconfigure

import com.goofy.client.starter.resttemplate.RestTemplateFactory
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.web.client.RestTemplate

@Primary
@Configuration
class RestTemplateAutoConfigure {
    @Primary
    @Bean
    fun restTemplate(
        config: RestTemplateConfig
    ): RestTemplate {
        return RestTemplateFactory().restTemplate(
            maxTotalConnect = config.maxTotalConnect,
            maxConnectPerRoute = config.maxConnectPerRoute,
            connectTimeout = config.connectTimeout,
            readTimeout = config.readTimeout
        )
    }

    /**
     * RestTemplate Config
     **/
    @Primary
    @Configuration
    @ConfigurationProperties(prefix = "goofy.starter.client.resttemplate")
    @ConfigurationPropertiesBinding
    class RestTemplateConfig {
        var maxTotalConnect: Int = 0
        var maxConnectPerRoute: Int = 100
        var connectTimeout: Int = 1000
        var readTimeout: Int = 1000
    }
}
