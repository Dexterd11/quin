package com.example.quin.controller

import com.example.quin.model.ContactOptionsResponse
import com.example.quin.service.SymptomService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/symptom-assessments")
class SymptomController(
    private val symptomService: SymptomService
) {
    @GetMapping("/{id}/contact-options")
    fun getContactOptions(
        @PathVariable id: String
    ): ResponseEntity<ContactOptionsResponse> {
        val response = symptomService.getById(id)
            ?: return ResponseEntity.notFound().build()
        return ResponseEntity.ok(response)
    }
}