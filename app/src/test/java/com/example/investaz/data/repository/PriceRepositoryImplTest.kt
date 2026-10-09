package com.example.investaz.data.repository

import com.example.investaz.data.local.dao.PriceDao
import com.example.investaz.data.local.entity.PriceEntity
import com.example.investaz.data.network.NetworkMonitor
import com.example.investaz.data.remote.PriceSocketClient
import com.example.investaz.data.remote.parser.PriceMessageParser
import com.example.investaz.domain.model.ConnectionStatus
import com.example.investaz.domain.model.PriceDirection
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal

@OptIn(ExperimentalCoroutinesApi::class)
class PriceRepositoryImplTest {

    private val socket = FakeSocketClient()
    private val dao = FakePriceDao()
    private val network = FakeNetworkMonitor()

    private fun createRepository(dispatcher: CoroutineDispatcher) =
        PriceRepositoryImpl(socket, network, dao, PriceMessageParser(), dispatcher, clock = { 0L })

    @Test
    fun `socket messages are cached and exposed from the database`() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        val repository = createRepository(dispatcher)
        val sync = launch(dispatcher) { repository.syncPrices() }

        socket.messages.emit(payload("EURUSD", "up", bid = "1.08240"))
        socket.messages.emit("garbage")
        socket.messages.emit(payload("EURUSD", "down", bid = "1.08100"))

        val item = repository.observePrices().first().single()
        assertEquals("EURUSD", item.symbol)
        assertEquals(BigDecimal("1.08100"), item.bid)
        assertEquals(PriceDirection.DOWN, item.direction)
        assertEquals(1_791_500_399_000L, item.updatedAt)

        sync.cancel()
    }

    @Test
    fun `connects on start and disconnects when sync is cancelled`() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        val repository = createRepository(dispatcher)
        val sync = launch(dispatcher) { repository.syncPrices() }
        assertEquals(ConnectionStatus.CONNECTING, socket.connectionStatus.value)

        sync.cancel()
        sync.join()
        assertEquals(ConnectionStatus.DISCONNECTED, socket.connectionStatus.value)
    }

    @Test
    fun `losing the network closes the socket and regaining it reconnects`() = runTest {
        val dispatcher = UnconfinedTestDispatcher(testScheduler)
        val repository = createRepository(dispatcher)
        val sync = launch(dispatcher) { repository.syncPrices() }

        network.isOnline.value = false
        assertEquals(ConnectionStatus.DISCONNECTED, socket.connectionStatus.value)

        network.isOnline.value = true
        assertEquals(ConnectionStatus.CONNECTING, socket.connectionStatus.value)

        sync.cancel()
    }

    @Test
    fun `cached data survives while offline`() = runTest {
        dao.upsertAll(listOf(PriceEntity("GOLD", "up", "1", "2", "0.5", "3", 1, 42L)))
        val repository = createRepository(UnconfinedTestDispatcher(testScheduler))

        assertEquals(ConnectionStatus.DISCONNECTED, repository.observeConnectionStatus().first())
        assertTrue(repository.observePrices().first().any { it.symbol == "GOLD" })
    }

    private fun payload(symbol: String, direction: String, bid: String) =
        """{"result":[{"0":"$direction","1":"$symbol","2":"$bid","3":"1.09","4":"1.07","5":"1.10","6":3,"7":"2026-10-08T22:59:59.000Z"}]}"""

    private class FakeSocketClient : PriceSocketClient {
        val messages = MutableSharedFlow<String>(extraBufferCapacity = 8)
        override val connectionStatus = MutableStateFlow(ConnectionStatus.DISCONNECTED)
        override fun messages(): Flow<String> = messages
        override fun connect() { connectionStatus.value = ConnectionStatus.CONNECTING }
        override fun disconnect() { connectionStatus.value = ConnectionStatus.DISCONNECTED }
    }

    private class FakeNetworkMonitor : NetworkMonitor {
        override val isOnline = MutableStateFlow(true)
    }

    private class FakePriceDao : PriceDao {
        private val rows = MutableStateFlow<Map<String, PriceEntity>>(emptyMap())
        override fun observeAll(): Flow<List<PriceEntity>> = rows.map { it.values.sortedBy(PriceEntity::symbol) }
        override suspend fun upsertAll(prices: List<PriceEntity>) {
            rows.value = rows.value + prices.associateBy { it.symbol }
        }
    }
}
