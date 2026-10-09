package com.example.investaz.domain.model

enum class PriceFilter {
    ALL, GAINERS, LOSERS;

    fun matches(item: PriceItem): Boolean = when (this) {
        ALL -> true
        GAINERS -> item.direction == PriceDirection.UP
        LOSERS -> item.direction == PriceDirection.DOWN
    }
}
