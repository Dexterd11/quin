package com.example.quin.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "api")
data class ApiConfiguration(
    val baseUrl: String
)
