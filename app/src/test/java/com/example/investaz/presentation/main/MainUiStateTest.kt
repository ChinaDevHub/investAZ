package com.example.investaz.presentation.main

import com.example.investaz.domain.model.ConnectionStatus
import com.example.investaz.domain.model.MarketSummary
import com.example.investaz.presentation.main.model.Placeholder
import com.example.investaz.presentation.main.model.PriceUiModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class MainUiStateTest {

    private val price = PriceUiModel("EURUSD", "EU", "1", "2", "0", "3", 1, "12:00:00", 50, 0, 0)
    private val cached = MarketSummary(total = 1, gainers = 1, losers = 0)

    @Test
    fun `cached prices are shown even when disconnected`() {
        assertNull(MainUiState(ConnectionStatus.DISCONNECTED, cached, prices = listOf(price)).placeholder)
    }

    @Test
    fun `empty filter result over a filled cache shows no matches`() {
        assertEquals(Placeholder.NO_MATCHES, MainUiState(ConnectionStatus.DISCONNECTED, cached).placeholder)
    }

    @Test
    fun `empty cache shows offline or loading placeholder`() {
        assertEquals(Placeholder.OFFLINE, MainUiState(ConnectionStatus.DISCONNECTED).placeholder)
        assertEquals(Placeholder.LOADING, MainUiState(ConnectionStatus.CONNECTING).placeholder)
        assertEquals(Placeholder.LOADING, MainUiState(ConnectionStatus.CONNECTED).placeholder)
    }
}
