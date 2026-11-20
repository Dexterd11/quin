package com.example.quin.model

data class ContactOptionsResponse(
    val assessmentId: String,
    val urgencyType: UrgencyType,
    val contactOptions: List<ContactOption>
)

data class ContactOption(
    val type: ContactType
)

