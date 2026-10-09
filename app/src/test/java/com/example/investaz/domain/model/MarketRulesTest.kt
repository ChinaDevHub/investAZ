package com.example.investaz.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test
import java.math.BigDecimal

class MarketRulesTest {

    private fun price(direction: PriceDirection, bid: String = "5", low: String = "0", high: String = "10") =
        PriceItem("X", BigDecimal(bid), BigDecimal(bid), BigDecimal(low), BigDecimal(high), 1, direction, 0L)

    @Test
    fun `summary counts gainers and losers and their share`() {
        val prices = listOf(
            price(PriceDirection.UP), price(PriceDirection.UP), price(PriceDirection.UP),
            price(PriceDirection.DOWN), price(PriceDirection.UNCHANGED),
        )

        val summary = MarketSummary.from(prices)

        assertEquals(MarketSummary(total = 5, gainers = 3, losers = 1), summary)
        assertEquals(75, summary.gainersPercent)
        assertEquals(3, summary.countFor(PriceFilter.GAINERS))
    }

    @Test
    fun `sentiment is neutral when nothing moved`() {
        assertEquals(50, MarketSummary.Empty.gainersPercent)
    }

    @Test
    fun `filters keep only matching directions`() {
        val prices = listOf(price(PriceDirection.UP), price(PriceDirection.DOWN), price(PriceDirection.UNCHANGED))

        assertEquals(3, prices.count(PriceFilter.ALL::matches))
        assertEquals(listOf(PriceDirection.UP), prices.filter(PriceFilter.GAINERS::matches).map { it.direction })
        assertEquals(listOf(PriceDirection.DOWN), prices.filter(PriceFilter.LOSERS::matches).map { it.direction })
    }

    @Test
    fun `range position is clamped and safe for empty ranges`() {
        assertEquals(0.25f, price(PriceDirection.UP, bid = "2.5").rangePosition, 0.0001f)
        assertEquals(1f, price(PriceDirection.UP, bid = "742455.56").rangePosition, 0f)
        assertEquals(0f, price(PriceDirection.UP, bid = "-1").rangePosition, 0f)
        assertEquals(0.5f, price(PriceDirection.UP, low = "3", high = "3").rangePosition, 0f)
    }
}
