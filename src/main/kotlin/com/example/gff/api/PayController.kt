package com.example.gff.api

import com.example.gff.api.model.PayRequest
import com.example.gff.api.model.PayResponse
import com.example.gff.service.PaymentService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class PayController(
    private val paymentService: PaymentService,
) {
    @PostMapping("/pay")
    fun pay(
        @RequestBody request: PayRequest,
    ): PayResponse {
        return paymentService.processPayment(request)
    }
}
