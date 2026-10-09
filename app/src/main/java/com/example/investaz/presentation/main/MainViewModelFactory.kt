package com.example.investaz.presentation.main

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.investaz.InvestAzApp
import com.example.investaz.domain.usecase.GetConnectionStatusUseCase
import com.example.investaz.domain.usecase.GetPricesUseCase
import com.example.investaz.domain.usecase.SyncPricesUseCase

object MainViewModelFactory {
    val Default: ViewModelProvider.Factory = viewModelFactory {
        initializer {
            val repository = (this[APPLICATION_KEY] as InvestAzApp).container.priceRepository
            MainViewModel(
                getPrices = GetPricesUseCase(repository),
                getConnectionStatus = GetConnectionStatusUseCase(repository),
                syncPrices = SyncPricesUseCase(repository),
            )
        }
    }
}
