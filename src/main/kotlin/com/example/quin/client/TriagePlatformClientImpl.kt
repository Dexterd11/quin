package com.example.quin.client

import com.example.quin.model.TriagePlatformResponse
import org.springframework.stereotype.Component

@Component
class TriagePlatformClientImpl(private val httpClient: OkHttpClientImpl) : TriagePlatformClient {
    override fun getById(id: String): TriagePlatformResponse? =
        httpClient.getById(id)
}