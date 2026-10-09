package com.example.investaz.domain.model

data class MarketSummary(val total: Int, val gainers: Int, val losers: Int) {

    /** Share of rising instruments among those that moved, 0..100 (50 when nothing moved). */
    val gainersPercent: Int
        get() {
            val moved = gainers + losers
            return if (moved == 0) NEUTRAL_PERCENT else gainers * 100 / moved
        }

    fun countFor(filter: PriceFilter): Int = when (filter) {
        PriceFilter.ALL -> total
        PriceFilter.GAINERS -> gainers
        PriceFilter.LOSERS -> losers
    }

    companion object {
        private const val NEUTRAL_PERCENT = 50

        val Empty = MarketSummary(total = 0, gainers = 0, losers = 0)

        fun from(prices: List<PriceItem>) = MarketSummary(
            total = prices.size,
            gainers = prices.count { it.direction == PriceDirection.UP },
            losers = prices.count { it.direction == PriceDirection.DOWN },
        )
    }
}
