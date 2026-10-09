package com.example.investaz.domain.model

import java.math.BigDecimal
import java.math.MathContext

data class PriceItem(
    val symbol: String,
    val bid: BigDecimal,
    val ask: BigDecimal,
    val low: BigDecimal,
    val high: BigDecimal,
    val spread: Int,
    val direction: PriceDirection,
    val updatedAt: Long,
) {
    /** Where the bid sits inside the day's low–high range: 0 = at the low, 1 = at the high. */
    val rangePosition: Float
        get() {
            val span = high - low
            if (span.signum() <= 0) return MIDDLE
            return (bid - low).divide(span, MathContext.DECIMAL32).toFloat().coerceIn(0f, 1f)
        }

    private companion object {
        const val MIDDLE = 0.5f
    }
}
