package com.example.quin.model

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.Instant
import java.util.UUID

data class TriagePlatformResponse(
    val id: UUID,
    val symptomText: String,
    @param:JsonProperty("urgency")
    val urgencyType: UrgencyType,
    val createdAt: Instant,
)
