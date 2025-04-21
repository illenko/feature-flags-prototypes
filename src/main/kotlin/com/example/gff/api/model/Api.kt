package com.example.gff.api.model

import java.util.UUID

data class PayRequest(
    val id: UUID,
    val terminalId: String,
    val amount: Long,
    val currency: String,
    val paymentMethod: String,
)

data class PayResponse(
    val paymentId: UUID,
    val amount: Long,
    val currency: String,
    val paymentMethod: String,
    val fee: Fee,
    val discount: Discount,
    val securityChecks: SecurityChecks,
    val uiCustomizationEnabled: Boolean,
)

data class Fee(
    val amount: Int,
    val percentage: Double,
)

data class Discount(
    val amount: Int,
    val percentage: Double,
    val program: String,
)

data class SecurityChecks(
    val fraudDetection: Boolean,
    val addressVerification: Boolean,
    val threeDS: Boolean,
)
