package com.example.investaz.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.investaz.domain.model.MarketSummary
import com.example.investaz.domain.model.PriceFilter
import com.example.investaz.domain.usecase.GetConnectionStatusUseCase
import com.example.investaz.domain.usecase.GetPricesUseCase
import com.example.investaz.domain.usecase.SyncPricesUseCase
import com.example.investaz.presentation.common.SyncController
import com.example.investaz.presentation.main.mapper.PriceUiMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    getPrices: GetPricesUseCase,
    getConnectionStatus: GetConnectionStatusUseCase,
    private val syncPrices: SyncPricesUseCase,
    uiMapper: PriceUiMapper = PriceUiMapper(),
    computationDispatcher: CoroutineDispatcher = Dispatchers.Default,
) : ViewModel(), SyncController {

    private val selectedFilter = MutableStateFlow(PriceFilter.ALL)

    val uiState: StateFlow<MainUiState> =
        combine(getPrices(), getConnectionStatus(), selectedFilter) { prices, status, filter ->
            MainUiState(
                connectionStatus = status,
                summary = MarketSummary.from(prices),
                filter = filter,
                prices = prices.filter(filter::matches).map(uiMapper::map),
            )
        }
            .flowOn(computationDispatcher)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(STOP_TIMEOUT_MS), MainUiState())

    private var syncJob: Job? = null

    fun onFilterSelected(filter: PriceFilter) {
        selectedFilter.value = filter
    }

    override fun startSync() {
        if (syncJob?.isActive == true) return
        syncJob = viewModelScope.launch { syncPrices() }
    }

    override fun stopSync() {
        syncJob?.cancel()
        syncJob = null
    }

    private companion object {
        const val STOP_TIMEOUT_MS = 5_000L
    }
}
