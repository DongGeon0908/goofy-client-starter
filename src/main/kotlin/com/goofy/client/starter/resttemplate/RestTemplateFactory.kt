package com.goofy.client.starter.resttemplate

import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClientBuilder
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.http.client.SimpleClientHttpRequestFactory
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.http.converter.StringHttpMessageConverter
import org.springframework.web.client.RestTemplate
import java.nio.charset.StandardCharsets

/**
 * RestTemplate Factory
 * easy to make RestTemplate
 **/
class RestTemplateFactory {
    fun restTemplate(
        maxTotalConnect: Int,
        maxConnectPerRoute: Int,
        connectTimeout: Int,
        readTimeout: Int
    ): RestTemplate {
        val clientHttpRequestFactory = createFactory(
            maxTotalConnect = maxTotalConnect,
            maxConnectPerRoute = maxConnectPerRoute,
            connectTimeout = connectTimeout,
            readTimeout = readTimeout
        )

        val restTemplate = RestTemplate(clientHttpRequestFactory)

        val converters: MutableList<HttpMessageConverter<*>> = restTemplate.messageConverters

        val converterTarget: HttpMessageConverter<*>? = converters.firstOrNull {
            StringHttpMessageConverter::class.java == it.javaClass
        }

        if (null != converterTarget) {
            converters.remove(converterTarget)
        }

        converters.add(1, StringHttpMessageConverter(StandardCharsets.UTF_8))

        return restTemplate
    }

    private fun createFactory(
        maxTotalConnect: Int,
        maxConnectPerRoute: Int,
        connectTimeout: Int,
        readTimeout: Int
    ): ClientHttpRequestFactory {
        if (isSimpleClient(maxTotalConnect)) {
            return simpleClientHttpRequestFactory(
                readTimeout = readTimeout,
                connectTimeout = connectTimeout
            )
        }

        return httpComponentsClientHttpRequestFactory(
            maxTotalConnect = maxTotalConnect,
            maxConnectPerRoute = maxConnectPerRoute,
            connectTimeout = connectTimeout,
            readTimeout = readTimeout
        )
    }

    private fun isSimpleClient(maxTotalConnect: Int): Boolean {
        return maxTotalConnect <= 0
    }

    private fun simpleClientHttpRequestFactory(
        readTimeout: Int,
        connectTimeout: Int
    ): SimpleClientHttpRequestFactory {
        return SimpleClientHttpRequestFactory()
            .apply {
                this.setConnectTimeout(readTimeout)
                this.setConnectTimeout(connectTimeout)
            }
    }

    private fun httpComponentsClientHttpRequestFactory(
        maxTotalConnect: Int,
        maxConnectPerRoute: Int,
        connectTimeout: Int,
        readTimeout: Int
    ): HttpComponentsClientHttpRequestFactory {
        val httpClient = httpClient(maxTotalConnect, maxConnectPerRoute)

        return HttpComponentsClientHttpRequestFactory(httpClient)
            .apply {
                this.setConnectTimeout(connectTimeout)
                this.setReadTimeout(readTimeout)
            }
    }

    private fun httpClient(
        maxTotalConnect: Int,
        maxConnectPerRoute: Int
    ): CloseableHttpClient {
        return HttpClientBuilder.create()
            .setMaxConnTotal(maxTotalConnect)
            .setMaxConnPerRoute(maxConnectPerRoute)
            .build()
    }
}
