package com.example.quin.config

import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HttpClientConfiguration {
    @Bean
    fun okHttpClient(): OkHttpClient = OkHttpClient.Builder()
        .build()
}