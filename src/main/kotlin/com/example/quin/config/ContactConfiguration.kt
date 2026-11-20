package com.example.quin.config

import org.springframework.boot.context.properties.ConfigurationProperties


@ConfigurationProperties(prefix = "contact")
data class ContactConfiguration(
    val callbackTimeFrom: Int,
    val callbackTimeTo: Int,
)