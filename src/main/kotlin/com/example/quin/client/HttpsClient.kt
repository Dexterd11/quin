package com.example.quin.client

import com.example.quin.model.TriagePlatformResponse

interface HttpsClient {
    fun getById(id: String): TriagePlatformResponse?
}