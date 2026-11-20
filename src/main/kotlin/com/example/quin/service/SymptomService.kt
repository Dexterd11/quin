package com.example.quin.service

import com.example.quin.client.TriagePlatformClientImpl
import com.example.quin.config.ContactConfiguration
import com.example.quin.model.ContactOption
import com.example.quin.model.ContactOptionsResponse
import com.example.quin.model.ContactType
import com.example.quin.model.TriagePlatformResponse
import com.example.quin.model.UrgencyType
import org.springframework.stereotype.Service
import java.time.Clock
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId

@Service
class SymptomService(
    private val client: TriagePlatformClientImpl,
    private val contactConfiguration: ContactConfiguration,
    private val clock: Clock
) {
    fun getById(id: String): ContactOptionsResponse? {
        val requestResponse = client.getById(id) ?: return null
        val urgencyType = requestResponse.urgencyType
        val contactType = when (urgencyType) {
            UrgencyType.HIGH -> if (Instant.now(clock).checkTimeWindow(contactConfiguration)) {
                listOf(
                    ContactOption(type = ContactType.CALLBACK_NUMBER),
                    ContactOption(type = ContactType.PHONE_CALL),
                    ContactOption(type = ContactType.CHAT)
                )
            } else {
                listOf(
                    ContactOption(type = ContactType.PHONE_CALL),
                    ContactOption(type = ContactType.CHAT)
                )
            }

            UrgencyType.MEDIUM ->
                listOf(
                    ContactOption(type = ContactType.PHONE_CALL),
                    ContactOption(type = ContactType.CHAT)
                )

            UrgencyType.LOW ->
                listOf(
                    ContactOption(type = ContactType.CHAT)
                )
        }
        return requestResponse.toContactOptionsResponse(
            contactType
        )
    }
}

private fun TriagePlatformResponse.toContactOptionsResponse(contactOptionList: List<ContactOption>): ContactOptionsResponse =
    ContactOptionsResponse(
        assessmentId = id.toString(),
        urgencyType = urgencyType,
        contactOptions = contactOptionList
    )

private fun Instant.checkTimeWindow(contactConfiguration: ContactConfiguration): Boolean {
    val currentTime = atZone(ZoneId.systemDefault())
        .toLocalTime()
    val startTime = LocalTime.of(contactConfiguration.callbackTimeFrom, 0)
    val endTime = LocalTime.of(contactConfiguration.callbackTimeTo, 0)
    return !currentTime.isBefore(startTime) && currentTime.isBefore(endTime)
}