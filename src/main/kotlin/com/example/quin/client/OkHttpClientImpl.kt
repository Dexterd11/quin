package com.example.quin.client

import com.example.quin.config.ApiConfiguration
import com.example.quin.model.TriagePlatformResponse
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.stereotype.Component
import java.io.IOException

@Component
class OkHttpClientImpl(
    private val client: OkHttpClient,
    private val apiConfiguration: ApiConfiguration,
    private val mapper: ObjectMapper
) : HttpsClient {
    private val logger = KotlinLogging.logger {}
    override fun getById(id: String): TriagePlatformResponse? {

        val request = Request.Builder()
            .url("${apiConfiguration.baseUrl}/assessments/$id")
            .get()
            .build()

        return client.newCall(request).execute().use { response ->
            if (response.code == 404) {
                return@use null
            }

            if (!response.isSuccessful) {
                throw IOException("Request failed: ${response.code}")
            }

            try {
                mapper.readValue(
                    response.body.string(),
                    TriagePlatformResponse::class.java
                )
            } catch (exception: JsonProcessingException) {
                logger.warn(exception) { "Failed to parse triage response" }
                null
            }
        }
    }
}
