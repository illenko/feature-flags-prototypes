package com.example.gff.api

import dev.openfeature.sdk.Client
import dev.openfeature.sdk.ImmutableContext
import dev.openfeature.sdk.Value
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class PayController(
    private val featureClient: Client,
) {
    @PostMapping("/pay")
    fun pay(
        @RequestBody request: PayRequest,
    ): PayResponse {
        val context =
            ImmutableContext(
                mapOf(
                    "terminalId" to Value(request.terminalId),
                    "amount" to Value(request.amount),
                    "currency" to Value(request.currency),
                    "paymentMethod" to Value(request.paymentMethod),
                ),
            )

        val isNewFlowEnabled =
            featureClient.getBooleanValue(
                "test-flow",
                false,
                context,
            )

        return if (isNewFlowEnabled) {
            PayResponse("New flow enabled for terminal ${request.terminalId}")
        } else {
            PayResponse("Old flow enabled for terminal ${request.terminalId}")
        }
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

data class GenerationData(
    val firstName: Boolean,
    val lastName: Boolean,
    val email: Boolean,
)
