package com.example.gff.service

import com.example.gff.api.model.Discount
import com.example.gff.api.model.Fee
import com.example.gff.api.model.PayRequest
import com.example.gff.api.model.PayResponse
import com.example.gff.api.model.SecurityChecks
import com.fasterxml.jackson.databind.ObjectMapper
import dev.openfeature.sdk.Client
import dev.openfeature.sdk.MutableContext
import mu.KotlinLogging
import org.springframework.stereotype.Service
import kotlin.math.roundToInt

@Service
class PaymentService(
    private val featureClient: Client,
    private val objectMapper: ObjectMapper,
) {
    private val log = KotlinLogging.logger {}

    fun processPayment(request: PayRequest): PayResponse {
        // Create a context with request data for feature flag evaluation
        val ctx = createContext(request)

        // Calculate fee
        val fee = getFee(request, ctx)

        // Calculate discount
        val discount = getDiscount(request, ctx)

        // Get security checks
        val securityChecks = getSecurityChecks(ctx)

        // Get UI customization setting
        val uiCustomizationEnabled = getUiCustomizationEnabled(ctx)

        // Build response with all feature flag information
        return buildPayResponse(
            request,
            fee,
            discount,
            securityChecks,
            uiCustomizationEnabled,
        )
    }

    private fun createContext(request: PayRequest): MutableContext =
        MutableContext(request.id.toString())
            .add("amount", request.amount.toInt())
            .add("currency", request.currency)
            .add("paymentMethod", request.paymentMethod)

    private fun getFee(
        request: PayRequest,
        context: MutableContext,
    ): Fee {
        // Number flag: paymentMethodFee - applies different fee percentages based on payment method
        val feePercentage =
            featureClient.getDoubleDetails(
                "paymentMethodFee",
                1.0, // default 1% fee
                context,
            )

        log.info { "Fee percentage: ${feePercentage.value}% (${feePercentage.reason} - ${feePercentage.variant})" }

        // Calculate fee amount
        val feeAmount = (request.amount * feePercentage.value / 100.0).roundToInt()

        return Fee(
            amount = feeAmount,
            percentage = feePercentage.value,
        )
    }

    private fun getDiscount(
        request: PayRequest,
        context: MutableContext,
    ): Discount {
        // String flag: discountProgram - enables different discount programs
        val discountProgram =
            featureClient.getStringDetails(
                "discountProgram",
                "none", // default no discount
                context,
            )

        log.info { "Discount program: ${discountProgram.value} (${discountProgram.reason} - ${discountProgram.variant})" }

        // Calculate discount based on program
        val discountPercentage =
            when (discountProgram.value) {
                "standard" -> 5.0
                "premium" -> 10.0
                "seasonal" -> 15.0
                else -> 0.0
            }

        val discountAmount = (request.amount * discountPercentage / 100.0).roundToInt()

        return Discount(
            amount = discountAmount,
            percentage = discountPercentage,
            program = discountProgram.value,
        )
    }

    private fun getSecurityChecks(context: MutableContext): SecurityChecks {
        // Object flag: securityChecks - enables/disables different security checks
        val securityChecks =
            featureClient.getObject(
                "securityChecks",
                defaultSecurityChecks,
                context,
            )

        log.info { "Security checks: $securityChecks" }

        return securityChecks
    }

    private fun getUiCustomizationEnabled(context: MutableContext): Boolean {
        // Boolean flag: uiCustomization - enables/disables UI customization
        val uiCustomizationEnabled =
            featureClient.getBooleanDetails(
                "uiCustomization",
                false, // default disabled
                context,
            )

        log.info {
            "UI customization enabled: ${uiCustomizationEnabled.value} (${uiCustomizationEnabled.reason} - ${uiCustomizationEnabled.variant})"
        }

        return uiCustomizationEnabled.value
    }

    private fun buildPayResponse(
        request: PayRequest,
        fee: Fee,
        discount: Discount,
        securityChecks: SecurityChecks,
        uiCustomizationEnabled: Boolean,
    ): PayResponse =
        PayResponse(
            paymentId = request.id,
            amount = request.amount,
            currency = request.currency,
            paymentMethod = request.paymentMethod,
            fee = fee,
            discount = discount,
            securityChecks = securityChecks,
            uiCustomizationEnabled = uiCustomizationEnabled,
        )

    private inline fun <reified T> Client.getObject(
        key: String,
        defaultValue: T,
        context: MutableContext,
    ): T =
        getStringDetails(key, objectMapper.writeValueAsString(defaultValue), context).let {
            try {
                log.info { "Retrieved $key: $it" }
                objectMapper.readValue(it.value, T::class.java)
            } catch (e: Exception) {
                log.error(e) { "Failed to parse object from string: $it" }
                defaultValue
            }
        }

    companion object {
        private val defaultSecurityChecks =
            SecurityChecks(
                fraudDetection = false,
                addressVerification = false,
                threeDS = false,
            )
    }
}
