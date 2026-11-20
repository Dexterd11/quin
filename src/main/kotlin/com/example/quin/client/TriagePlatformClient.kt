package com.example.quin.client

import com.example.quin.model.TriagePlatformResponse

interface TriagePlatformClient {
    fun getById(id: String): TriagePlatformResponse?
}