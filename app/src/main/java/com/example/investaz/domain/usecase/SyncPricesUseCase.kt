package com.example.investaz.domain.usecase

import com.example.investaz.domain.repository.PriceRepository

class SyncPricesUseCase(private val repository: PriceRepository) {
    suspend operator fun invoke() = repository.syncPrices()
}
