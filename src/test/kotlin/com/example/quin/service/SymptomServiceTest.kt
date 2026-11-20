package com.example.quin.service

import com.example.quin.client.TriagePlatformClientImpl
import com.example.quin.config.ContactConfiguration
import com.example.quin.model.ContactType
import com.example.quin.model.TriagePlatformResponse
import com.example.quin.model.UrgencyType
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import java.time.Clock
import java.time.LocalDate
import java.time.ZoneId
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SymptomServiceTest {
    private val client = mock(TriagePlatformClientImpl::class.java)
    private val zone = ZoneId.systemDefault()

    @Test
    fun `high urgency at 08 00 includes callback`() {
        givenAssessment(UrgencyType.HIGH)

        val result = serviceAt(8).getById("assessment-id")

        assertEquals(
            listOf(ContactType.CALLBACK_NUMBER, ContactType.PHONE_CALL, ContactType.CHAT),
            result?.contactOptions?.map { it.type }
        )
    }

    @Test
    fun `high urgency at 12 00 excludes callback`() {
        givenAssessment(UrgencyType.HIGH)

        val result = serviceAt(12).getById("assessment-id")

        assertEquals(
            listOf(ContactType.PHONE_CALL, ContactType.CHAT),
            result?.contactOptions?.map { it.type }
        )
    }

    @Test
    fun `medium urgency returns phone and chat`() {
        givenAssessment(UrgencyType.MEDIUM)

        val result = serviceAt(10).getById("assessment-id")

        assertEquals(
            listOf(ContactType.PHONE_CALL, ContactType.CHAT),
            result?.contactOptions?.map { it.type }
        )
    }

    @Test
    fun `low urgency returns chat only`() {
        givenAssessment(UrgencyType.LOW)

        val result = serviceAt(10).getById("assessment-id")

        assertEquals(listOf(ContactType.CHAT), result?.contactOptions?.map { it.type })
    }

    @Test
    fun `missing assessment returns no result`() {
        `when`(client.getById("missing-id")).thenReturn(null)

        val result = serviceAt(10).getById("missing-id")

        assertNull(result)
    }

    private fun givenAssessment(urgencyType: UrgencyType) {
        `when`(client.getById("assessment-id")).thenReturn(
            TriagePlatformResponse(
                id = UUID.randomUUID(),
                symptomText = "Headache",
                urgencyType = urgencyType,
                createdAt = LocalDate.of(2026, 1, 1)
                    .atStartOfDay(zone)
                    .toInstant()
            )
        )
    }

    private fun serviceAt(hour: Int): SymptomService = SymptomService(
        client = client,
        contactConfiguration = ContactConfiguration(
            callbackTimeFrom = 8,
            callbackTimeTo = 12
        ),
        clock = Clock.fixed(
            LocalDate.of(2026, 1, 1)
                .atTime(hour, 0)
                .atZone(zone)
                .toInstant(),
            zone
        )
    )
}
