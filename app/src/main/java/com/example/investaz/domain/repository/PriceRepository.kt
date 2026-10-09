package com.example.investaz.domain.repository

import com.example.investaz.domain.model.ConnectionStatus
import com.example.investaz.domain.model.PriceItem
import kotlinx.coroutines.flow.Flow

interface PriceRepository {

    /** Cached prices; the local database is the single source of truth. */
    fun observePrices(): Flow<List<PriceItem>>

    fun observeConnectionStatus(): Flow<ConnectionStatus>

    /** Streams live prices into the cache until the calling coroutine is cancelled. */
    suspend fun syncPrices()
}
