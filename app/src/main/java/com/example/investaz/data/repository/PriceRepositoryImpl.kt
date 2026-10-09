package com.example.investaz.data.repository

import android.util.Log
import com.example.investaz.data.local.dao.PriceDao
import com.example.investaz.data.local.entity.PriceEntity
import com.example.investaz.data.mapper.toDomain
import com.example.investaz.data.mapper.toEntity
import com.example.investaz.data.network.NetworkMonitor
import com.example.investaz.data.remote.PriceSocketClient
import com.example.investaz.data.remote.parser.PriceMessageParser
import com.example.investaz.domain.model.ConnectionStatus
import com.example.investaz.domain.model.PriceItem
import com.example.investaz.domain.repository.PriceRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

/** Socket -> Room -> Flow: the UI only ever reads from the database. */
class PriceRepositoryImpl(
    private val socketClient: PriceSocketClient,
    private val networkMonitor: NetworkMonitor,
    private val priceDao: PriceDao,
    private val parser: PriceMessageParser,
    private val ioDispatcher: CoroutineDispatcher,
    private val clock: () -> Long = System::currentTimeMillis,
) : PriceRepository {

    override fun observePrices(): Flow<List<PriceItem>> = priceDao.observeAll()
        .map { entities -> entities.map(PriceEntity::toDomain) }
        .flowOn(ioDispatcher)

    override fun observeConnectionStatus(): Flow<ConnectionStatus> = socketClient.connectionStatus

    override suspend fun syncPrices() = withContext(ioDispatcher) {
        // Losing the network cancels the stream (closing the socket); regaining it reconnects at once.
        networkMonitor.isOnline.collectLatest { online -> if (online) streamPricesIntoCache() }
    }

    private suspend fun streamPricesIntoCache() {
        socketClient.messages()
            .onStart { socketClient.connect() }
            .onCompletion { socketClient.disconnect() }
            .map(parser::parse)
            .filter { it.isNotEmpty() }
            .distinctUntilChanged()
            .onEach { dtos -> priceDao.upsertAll(dtos.map { it.toEntity(clock()) }) }
            .catch { e -> Log.e(TAG, "Price sync stopped", e) }
            .collect()
    }

    private companion object {
        const val TAG = "PriceRepository"
    }
}
