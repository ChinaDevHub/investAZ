package com.example.investaz.domain.usecase

import com.example.investaz.domain.model.ConnectionStatus
import com.example.investaz.domain.repository.PriceRepository
import kotlinx.coroutines.flow.Flow

class GetConnectionStatusUseCase(private val repository: PriceRepository) {
    operator fun invoke(): Flow<ConnectionStatus> = repository.observeConnectionStatus()
}
