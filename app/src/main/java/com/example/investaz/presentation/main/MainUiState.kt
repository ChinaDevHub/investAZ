package com.example.investaz.presentation.main

import com.example.investaz.domain.model.ConnectionStatus
import com.example.investaz.domain.model.MarketSummary
import com.example.investaz.domain.model.PriceFilter
import com.example.investaz.presentation.main.model.Placeholder
import com.example.investaz.presentation.main.model.PriceUiModel

data class MainUiState(
    val connectionStatus: ConnectionStatus = ConnectionStatus.CONNECTING,
    val summary: MarketSummary = MarketSummary.Empty,
    val filter: PriceFilter = PriceFilter.ALL,
    /** Prices matching [filter]. */
    val prices: List<PriceUiModel> = emptyList(),
) {
    val hasCachedPrices: Boolean get() = summary.total > 0

    /** Shown instead of the list when there is nothing to display. */
    val placeholder: Placeholder?
        get() = when {
            prices.isNotEmpty() -> null
            hasCachedPrices -> Placeholder.NO_MATCHES
            connectionStatus == ConnectionStatus.DISCONNECTED -> Placeholder.OFFLINE
            else -> Placeholder.LOADING
        }
}
