package com.example.investaz.domain.usecase

import com.example.investaz.domain.model.PriceItem
import com.example.investaz.domain.repository.PriceRepository
import kotlinx.coroutines.flow.Flow

class GetPricesUseCase(private val repository: PriceRepository) {
    operator fun invoke(): Flow<List<PriceItem>> = repository.observePrices()
}
