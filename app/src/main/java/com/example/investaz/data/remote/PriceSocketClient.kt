package com.example.investaz.data.remote

import com.example.investaz.domain.model.ConnectionStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface PriceSocketClient {
    val connectionStatus: StateFlow<ConnectionStatus>

    /** Raw payloads of the price event, emitted while collected. */
    fun messages(): Flow<String>

    fun connect()

    fun disconnect()
}
