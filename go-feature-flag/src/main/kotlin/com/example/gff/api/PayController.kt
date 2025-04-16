package com.example.gff.api

import dev.openfeature.sdk.Client
import dev.openfeature.sdk.MutableContext
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class PayController(
    private val featureClient: Client,
) {
    private val log = KotlinLogging.logger {}

    @PostMapping("/pay")
    fun pay(
        @RequestBody request: PayRequest,
    ): PayResponse {
        val isNewFlowEnabled =
            featureClient.getBooleanValue(
                "test-flow",
                false,
                MutableContext(request.terminalId)
                    .add("amount", request.amount.toInt())
                    .add("currency", request.currency)
                    .add("paymentMethod", request.paymentMethod),
            )

        val isNewFlowEnabledDetails =
            featureClient.getBooleanDetails(
                "test-flow",
                false,
                MutableContext(request.terminalId)
                    .add("amount", request.amount.toInt())
                    .add("currency", request.currency)
                    .add("paymentMethod", request.paymentMethod),
            )

        log.info {
            "Feature flag details: ${isNewFlowEnabledDetails.reason} - ${isNewFlowEnabledDetails.variant}"
        }

        return PayResponse(
            "${if (isNewFlowEnabled) "NEW" else "OLD"} flow applied for terminal ${request.terminalId}, " +
                "amount ${request.amount}, currency ${request.currency}, payment method ${request.paymentMethod}",
        )
    }
}

data class PayRequest(
    val id: UUID,
    val terminalId: String,
    val amount: Long,
    val currency: String,
    val paymentMethod: String,
)

data class PayResponse(
    val details: String,
)
